package br.com.urbanlink.dominio;

import java.util.ArrayList;

public interface RepositorioUsuarios {

    void adicionar(Usuario usuario);

    boolean atualizar(Usuario usuario);

    boolean remover(String id);

    /** Retorna null se o usuário não existir. */
    Usuario buscarPorId(String id);

    ArrayList<Usuario> obterTodos();

    void depositar(String id, double pontos);

    /** Débito atômico: retorna false (sem debitar) se o saldo for insuficiente. */
    boolean debitar(String id, double pontos);

    void registrarResgate(String id);
}
