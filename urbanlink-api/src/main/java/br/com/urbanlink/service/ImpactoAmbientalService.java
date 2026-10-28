package br.com.urbanlink.service;

import br.com.urbanlink.dominio.ImpactoAmbiental;
import br.com.urbanlink.dominio.RepositorioImpactos;
import br.com.urbanlink.dominio.RepositorioUsuarios;

import java.util.ArrayList;

public class ImpactoAmbientalService {

    private RepositorioImpactos repositorioImpactos;
    private RepositorioUsuarios repositorioUsuarios;

    public ImpactoAmbientalService(RepositorioImpactos repositorioImpactos, RepositorioUsuarios repositorioUsuarios) {
        this.repositorioImpactos = repositorioImpactos;
        this.repositorioUsuarios = repositorioUsuarios;
    }

    /** Retorna null se o usuário não existir; se ele ainda não usou voucher, retorna um impacto zerado. */
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
}
