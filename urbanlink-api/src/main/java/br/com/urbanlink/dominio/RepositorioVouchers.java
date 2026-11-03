package br.com.urbanlink.dominio;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface RepositorioVouchers {

    void adicionar(Voucher voucher);

    Voucher buscarPorCodigo(String codigo);

    ArrayList<Voucher> obterTodos();

    ArrayList<Voucher> obterPorUsuario(String usuarioId);

    int contarEmitidosDesde(String usuarioId, LocalDateTime desde);

    boolean marcarComoUtilizado(String codigo);

    boolean alterarStatusSeAtivo(String codigo, StatusVoucher novoStatus);

    boolean remover(String codigo);
}
