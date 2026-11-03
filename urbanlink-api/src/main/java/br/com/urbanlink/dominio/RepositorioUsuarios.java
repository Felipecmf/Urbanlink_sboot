package br.com.urbanlink.dominio;

import java.util.ArrayList;

public interface RepositorioUsuarios {

    void adicionar(Usuario usuario);

    boolean atualizar(Usuario usuario);

    boolean remover(String id);

    Usuario buscarPorId(String id);

    ArrayList<Usuario> obterTodos();

    void depositar(String id, double pontos);

    boolean debitar(String id, double pontos);

    void registrarResgate(String id);
}
