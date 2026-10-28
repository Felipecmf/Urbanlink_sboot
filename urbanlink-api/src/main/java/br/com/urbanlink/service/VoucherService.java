package br.com.urbanlink.service;

import br.com.urbanlink.dominio.ImpactoAmbiental;
import br.com.urbanlink.dominio.RepositorioImpactos;
import br.com.urbanlink.dominio.RepositorioTransportes;
import br.com.urbanlink.dominio.RepositorioUsuarios;
import br.com.urbanlink.dominio.RepositorioVouchers;
import br.com.urbanlink.dominio.StatusVoucher;
import br.com.urbanlink.dominio.TransporteIntegrado;
import br.com.urbanlink.dominio.TransportePublico;
import br.com.urbanlink.dominio.Usuario;
import br.com.urbanlink.dominio.UsuarioPremium;
import br.com.urbanlink.dominio.Voucher;
import br.com.urbanlink.dto.ConversaoRequest;
import br.com.urbanlink.util.Numeros;
import br.com.urbanlink.util.Validador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/** Regras centrais do UrbanLink: conversão de pontos em voucher, uso, cancelamento e antifraude. */
public class VoucherService {

    private static final int LIMITE_DIARIO_VOUCHERS = 5;

    private RepositorioVouchers repositorioVouchers;
    private RepositorioUsuarios repositorioUsuarios;
    private RepositorioTransportes repositorioTransportes;
    private RepositorioImpactos repositorioImpactos;

    public VoucherService(RepositorioVouchers repositorioVouchers, RepositorioUsuarios repositorioUsuarios,
                          RepositorioTransportes repositorioTransportes, RepositorioImpactos repositorioImpactos) {
        this.repositorioVouchers = repositorioVouchers;
        this.repositorioUsuarios = repositorioUsuarios;
        this.repositorioTransportes = repositorioTransportes;
        this.repositorioImpactos = repositorioImpactos;
    }

    /** Valor do crédito e pontos são SEMPRE calculados aqui; o cliente só informa o percentual. */
    public Voucher converter(ConversaoRequest pedido) {
        String usuarioId = Validador.texto(pedido.getUsuarioId(), "usuarioId", 3, 20);
        String transporteId = Validador.texto(pedido.getTransporteId(), "transporteId", 3, 20);
        double percentual = Validador.numero(pedido.getPercentualCobertura(), "percentualCobertura", 0.01, 100);

        Usuario usuario = repositorioUsuarios.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado: " + usuarioId);
        }
        TransportePublico transporte = repositorioTransportes.buscarPorId(transporteId);
        if (transporte == null) {
            throw new IllegalArgumentException("Transporte não encontrado: " + transporteId);
        }
        if (!transporte.isIntegrado() || !(transporte instanceof TransporteIntegrado)
                || !((TransporteIntegrado) transporte).isAceitaVoucher()) {
            throw new IllegalArgumentException("O transporte '" + transporte.getNome() + "' não aceita voucher.");
        }

        // Antifraude: limite diário de vouchers por usuário
        LocalDateTime inicioDoDia = LocalDate.now().atStartOfDay();
        if (repositorioVouchers.contarEmitidosDesde(usuarioId, inicioDoDia) >= LIMITE_DIARIO_VOUCHERS) {
            System.out.println("[AUDITORIA] ANTIFRAUDE limite diário: usuario=" + usuarioId);
            throw new IllegalArgumentException("Limite diário de " + LIMITE_DIARIO_VOUCHERS + " vouchers atingido.");
        }

        double pontos = transporte.calcularPontosNecessarios(percentual);
        double valor = transporte.calcularValorCredito(percentual);

        // Polimorfismo: o usuário premium paga menos pontos e tem limite mensal de resgates
        boolean premium = usuario instanceof UsuarioPremium;
        if (premium) {
            UsuarioPremium up = (UsuarioPremium) usuario;
            if (!up.podeResgatar()) {
                throw new IllegalArgumentException("Limite mensal de resgates atingido.");
            }
            pontos = Numeros.arredondar(pontos / up.getMultiplicadorBonus(), 2);
        }

        if (!repositorioUsuarios.debitar(usuarioId, pontos)) {
            throw new IllegalArgumentException("Saldo de pontos insuficiente. Necessário: " + pontos);
        }

        Voucher voucher = new Voucher(Validador.gerarId("SUP"), usuarioId, transporteId, valor, pontos, LocalDateTime.now());
        try {
            repositorioVouchers.adicionar(voucher);
            if (premium) {
                repositorioUsuarios.registrarResgate(usuarioId);
            }
        } catch (RuntimeException e) {
            repositorioUsuarios.depositar(usuarioId, pontos);   // devolve os pontos se não conseguiu gerar o voucher
            throw e;
        }
        System.out.println("[AUDITORIA] Voucher emitido: " + voucher.getCodigo() + " usuario=" + usuarioId
                + " valor=" + valor + " pontos=" + pontos);
        return voucher;
    }

    /** Só o dono usa o voucher. Retorna null se o voucher não existir. */
    public Voucher utilizar(String codigo, String usuarioId) {
        String dono = Validador.texto(usuarioId, "usuarioId", 3, 20);
        Voucher voucher = repositorioVouchers.buscarPorCodigo(codigo);
        if (voucher == null) {
            return null;
        }
        if (!voucher.getUsuarioId().equals(dono)) {
            System.out.println("[AUDITORIA] ANTIFRAUDE voucher de outro usuário: " + codigo + " tentativa=" + dono);
            throw new IllegalArgumentException("Este voucher não pertence ao usuário informado.");
        }
        voucher.utilizar();   // valida antifraude e status
        if (!repositorioVouchers.marcarComoUtilizado(codigo)) {
            throw new IllegalArgumentException("O voucher já foi utilizado ou alterado por outra requisição.");
        }

        TransportePublico transporte = repositorioTransportes.buscarPorId(voucher.getTransporteId());
        if (transporte != null) {
            registrarImpacto(dono, transporte.getCo2EvitadoPorUso(), voucher.getPontosUsados());
            if (transporte instanceof TransporteIntegrado) {
                repositorioTransportes.incrementarViagens(transporte.getId());
            }
        }
        System.out.println("[AUDITORIA] Voucher utilizado: " + codigo + " usuario=" + dono);
        return repositorioVouchers.buscarPorCodigo(codigo);
    }

    /** Permite apenas CANCELADO (devolve os pontos) ou EXPIRADO, e só a partir de ATIVO. Retorna null se não existir. */
    public Voucher alterarStatus(String codigo, StatusVoucher novoStatus) {
        if (novoStatus == null) {
            throw new IllegalArgumentException("O campo 'status' é obrigatório.");
        }
        if (novoStatus != StatusVoucher.CANCELADO && novoStatus != StatusVoucher.EXPIRADO) {
            throw new IllegalArgumentException("Use CANCELADO ou EXPIRADO. Para UTILIZADO, use PUT /vouchers/{codigo}/utilizar.");
        }
        Voucher voucher = repositorioVouchers.buscarPorCodigo(codigo);
        if (voucher == null) {
            return null;
        }
        if (!repositorioVouchers.alterarStatusSeAtivo(codigo, novoStatus)) {
            throw new IllegalArgumentException("Só é possível alterar vouchers ATIVOS (status atual: " + voucher.getStatus() + ").");
        }
        if (novoStatus == StatusVoucher.CANCELADO) {
            repositorioUsuarios.depositar(voucher.getUsuarioId(), voucher.getPontosUsados());
        }
        System.out.println("[AUDITORIA] Voucher " + codigo + " -> " + novoStatus);
        return repositorioVouchers.buscarPorCodigo(codigo);
    }

    /** Remove o voucher; se ainda estava ATIVO, devolve os pontos. Retorna false se não existir. */
    public boolean remover(String codigo) {
        Voucher voucher = repositorioVouchers.buscarPorCodigo(codigo);
        if (voucher == null) {
            return false;
        }
        repositorioVouchers.remover(codigo);
        if (voucher.getStatus() == StatusVoucher.ATIVO) {
            repositorioUsuarios.depositar(voucher.getUsuarioId(), voucher.getPontosUsados());
        }
        System.out.println("[AUDITORIA] Voucher removido: " + codigo);
        return true;
    }

    public Voucher buscar(String codigo) {
        return repositorioVouchers.buscarPorCodigo(codigo);
    }

    public ArrayList<Voucher> listar() {
        return repositorioVouchers.obterTodos();
    }

    public ArrayList<Voucher> listarPorUsuario(String usuarioId) {
        return repositorioVouchers.obterPorUsuario(usuarioId);
    }

    private void registrarImpacto(String usuarioId, double co2Evitado, double pontosUsados) {
        ImpactoAmbiental impacto = repositorioImpactos.buscarPorUsuario(usuarioId);
        if (impacto == null) {
            impacto = new ImpactoAmbiental(usuarioId);
        }
        impacto.registrarViagem(co2Evitado, pontosUsados);
        repositorioImpactos.salvar(impacto);
    }
}
