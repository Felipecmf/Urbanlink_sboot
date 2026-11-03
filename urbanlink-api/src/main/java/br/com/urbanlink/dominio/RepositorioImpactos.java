package br.com.urbanlink.dominio;

import java.util.ArrayList;

public interface RepositorioImpactos {

    ImpactoAmbiental buscarPorUsuario(String usuarioId);

    ArrayList<ImpactoAmbiental> obterTodos();

    void salvar(ImpactoAmbiental impacto);

    boolean remover(String usuarioId);
}