package br.com.urbanlink.service;

import br.com.urbanlink.dominio.ImpactoAmbiental;
import br.com.urbanlink.dominio.RepositorioImpactos;
import br.com.urbanlink.dominio.RepositorioUsuarios;
import br.com.urbanlink.util.Numeros;
import br.com.urbanlink.util.Validador;

import java.util.ArrayList;

public class ImpactoAmbientalService {

    private RepositorioImpactos repositorioImpactos;
    private RepositorioUsuarios repositorioUsuarios;

    public ImpactoAmbientalService(RepositorioImpactos repositorioImpactos, RepositorioUsuarios repositorioUsuarios) {
        this.repositorioImpactos = repositorioImpactos;
        this.repositorioUsuarios = repositorioUsuarios;
    }

    public ImpactoAmbiental buscarPorUsuario(String usuarioId) {
        if (repositorioUsuarios.buscarPorId(usuarioId) == null) {
            return null;
        }
        ImpactoAmbiental impacto = repositorioImpactos.buscarPorUsuario(usuarioId);
        if (impacto == null) {
            impacto = new ImpactoAmbiental(usuarioId);
        }
        return impacto;
    }

    public ArrayList<ImpactoAmbiental> listar() {
        return repositorioImpactos.obterTodos();
    }

    public ImpactoAmbiental adicionar(ImpactoAmbiental impacto) {
        String usuarioId = Validador.texto(impacto.getUsuarioId(), "usuarioId", 3, 20);
        if (repositorioUsuarios.buscarPorId(usuarioId) == null) {
            throw new IllegalArgumentException("Usuário não encontrado: " + usuarioId);
        }
        if (repositorioImpactos.buscarPorUsuario(usuarioId) != null) {
            throw new IllegalArgumentException(
                    "Este usuário já possui impacto registrado. Use PUT /impactos/" + usuarioId + ".");
        }
        impacto.setUsuarioId(usuarioId);
        validarEPreparar(impacto);
        repositorioImpactos.salvar(impacto);
        System.out.println("[AUDITORIA] Impacto ambiental criado: " + usuarioId);
        return repositorioImpactos.buscarPorUsuario(usuarioId);
    }

    public ImpactoAmbiental atualizar(String usuarioId, ImpactoAmbiental novo) {
        if (repositorioUsuarios.buscarPorId(usuarioId) == null) {
            return null;
        }
        novo.setUsuarioId(usuarioId);
        validarEPreparar(novo);
        repositorioImpactos.salvar(novo);
        System.out.println("[AUDITORIA] Impacto ambiental atualizado: " + usuarioId);
        return repositorioImpactos.buscarPorUsuario(usuarioId);
    }

    public boolean remover(String usuarioId) {
        boolean removido = repositorioImpactos.remover(usuarioId);
        if (removido) {
            System.out.println("[AUDITORIA] Impacto ambiental removido: " + usuarioId);
        }
        return removido;
    }

    private void validarEPreparar(ImpactoAmbiental impacto) {
        double co2 = Validador.numero(impacto.getTotalCo2Evitado(), "totalCo2Evitado", 0, 1000000);
        Validador.inteiro(impacto.getTotalViagens(), "totalViagens", 0, 1000000);
        Validador.numero(impacto.getPontosInvestidos(), "pontosInvestidos", 0, 1000000);
        impacto.setEquivalenciaArvores(Numeros.arredondar(co2 / ImpactoAmbiental.KG_CO2_POR_ARVORE_ANO, 2));
    }
}