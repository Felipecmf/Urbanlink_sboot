package br.com.urbanlink.dominio;

import java.util.ArrayList;

public interface RepositorioTransportes {

    void adicionar(TransportePublico transporte);

    boolean atualizar(TransportePublico transporte);

    boolean remover(String id);

    /** Retorna null se o transporte não existir. */
    TransportePublico buscarPorId(String id);

    ArrayList<TransportePublico> obterTodos();

    void incrementarViagens(String id);
}
