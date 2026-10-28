package br.com.urbanlink.dominio;

import java.util.ArrayList;

public interface RepositorioImpactos {

    /** Retorna null se o usuário ainda não tiver impacto registrado. */
    ImpactoAmbiental buscarPorUsuario(String usuarioId);

    ArrayList<ImpactoAmbiental> obterTodos();

    /** Atualiza o registro do usuário ou cria, se ainda não existir. */
    void salvar(ImpactoAmbiental impacto);
}
