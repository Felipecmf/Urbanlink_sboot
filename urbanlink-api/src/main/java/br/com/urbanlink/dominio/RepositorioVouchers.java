package br.com.urbanlink.dominio;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface RepositorioVouchers {

    void adicionar(Voucher voucher);

    /** Retorna null se o voucher não existir. */
    Voucher buscarPorCodigo(String codigo);

    ArrayList<Voucher> obterTodos();

    ArrayList<Voucher> obterPorUsuario(String usuarioId);

    /** Quantos vouchers o usuário emitiu desde o instante informado (antifraude). */
    int contarEmitidosDesde(String usuarioId, LocalDateTime desde);

    /** Só altera se o voucher ainda estiver ATIVO e aprovado no antifraude. Retorna false se não alterou. */
    boolean marcarComoUtilizado(String codigo);

    /** Só altera se o voucher ainda estiver ATIVO. Retorna false se não alterou. */
    boolean alterarStatusSeAtivo(String codigo, StatusVoucher novoStatus);

    boolean remover(String codigo);
}
