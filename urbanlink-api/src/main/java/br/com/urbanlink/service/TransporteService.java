package br.com.urbanlink.service;

import br.com.urbanlink.dominio.RepositorioTransportes;
import br.com.urbanlink.dominio.TransporteIntegrado;
import br.com.urbanlink.dominio.TransportePublico;
import br.com.urbanlink.util.Validador;

import java.util.ArrayList;

public class TransporteService {

    private RepositorioTransportes repositorio;

    public TransporteService(RepositorioTransportes repositorio) {
        this.repositorio = repositorio;
    }

    public TransportePublico adicionar(TransportePublico transporte) {
        validarDadosBasicos(transporte);
        transporte.setId(Validador.id(transporte.getId(), "TRP"));
        repositorio.adicionar(transporte);
        System.out.println("[AUDITORIA] Transporte criado: " + transporte.getId());
        return transporte;
    }

    public TransporteIntegrado adicionarIntegrado(TransporteIntegrado transporte) {
        validarDadosBasicos(transporte);
        validarDadosIntegrado(transporte);
        transporte.setId(Validador.id(transporte.getId(), "TRP"));
        transporte.setTotalViagensProcessadas(0);
        repositorio.adicionar(transporte);
        System.out.println("[AUDITORIA] Transporte integrado criado: " + transporte.getId());
        return transporte;
    }

    /** Retorna null se o transporte não existir. */
    public TransportePublico atualizar(String id, TransportePublico novo) {
        TransportePublico existente = repositorio.buscarPorId(id);
        if (existente == null) {
            return null;
        }
        if (existente instanceof TransporteIntegrado) {
            throw new IllegalArgumentException("Este transporte é integrado. Use PUT /transportes/integrados/" + id);
        }
        validarDadosBasicos(novo);
        novo.setId(id);
        repositorio.atualizar(novo);
        System.out.println("[AUDITORIA] Transporte atualizado: " + id);
        return repositorio.buscarPorId(id);
    }

    /** Retorna null se o transporte não existir. */
    public TransportePublico atualizarIntegrado(String id, TransporteIntegrado novo) {
        TransportePublico existente = repositorio.buscarPorId(id);
        if (existente == null) {
            return null;
        }
        validarDadosBasicos(novo);
        validarDadosIntegrado(novo);
        novo.setId(id);
        repositorio.atualizar(novo);
        System.out.println("[AUDITORIA] Transporte integrado atualizado: " + id);
        return repositorio.buscarPorId(id);
    }

    public boolean remover(String id) {
        boolean removido = repositorio.remover(id);
        if (removido) {
            System.out.println("[AUDITORIA] Transporte removido: " + id);
        }
        return removido;
    }

    public TransportePublico buscar(String id) {
        return repositorio.buscarPorId(id);
    }

    public ArrayList<TransportePublico> listar() {
        return repositorio.obterTodos();
    }

    private void validarDadosBasicos(TransportePublico t) {
        t.setNome(Validador.texto(t.getNome(), "nome", 2, 100));
        t.setModalidade(Validador.texto(t.getModalidade(), "modalidade", 2, 50));
        Validador.numero(t.getTarifaBase(), "tarifaBase", 0.01, 1000);
        Validador.numero(t.getCo2EvitadoPorUso(), "co2EvitadoPorUso", 0, 1000);
        t.setIntegrado(true);
    }

    private void validarDadosIntegrado(TransporteIntegrado t) {
        t.setSistemaBilhetagem(Validador.texto(t.getSistemaBilhetagem(), "sistemaBilhetagem", 2, 60));
    }
}
