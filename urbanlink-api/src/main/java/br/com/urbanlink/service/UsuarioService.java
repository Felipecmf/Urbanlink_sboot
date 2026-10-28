package br.com.urbanlink.service;

import br.com.urbanlink.dominio.RepositorioUsuarios;
import br.com.urbanlink.dominio.Usuario;
import br.com.urbanlink.dominio.UsuarioPremium;
import br.com.urbanlink.util.Validador;

import java.util.ArrayList;

public class UsuarioService {

    private RepositorioUsuarios repositorio;

    public UsuarioService(RepositorioUsuarios repositorio) {
        this.repositorio = repositorio;
    }

    public Usuario adicionar(Usuario usuario) {
        validarDadosBasicos(usuario);
        usuario.setId(Validador.id(usuario.getId(), "USR"));
        usuario.setSaldoPontos(0.0);   // o saldo nunca vem do cliente
        repositorio.adicionar(usuario);
        System.out.println("[AUDITORIA] Usuário criado: " + usuario.getId());
        return usuario;
    }

    public UsuarioPremium adicionarPremium(UsuarioPremium premium) {
        validarDadosBasicos(premium);
        validarDadosPremium(premium);
        premium.setId(Validador.id(premium.getId(), "USR"));
        premium.setSaldoPontos(0.0);
        premium.setResgatesRealizadosMes(0);
        repositorio.adicionar(premium);
        System.out.println("[AUDITORIA] Usuário premium criado: " + premium.getId());
        return premium;
    }

    /** Retorna null se o usuário não existir. */
    public Usuario atualizar(String id, Usuario novo) {
        Usuario existente = repositorio.buscarPorId(id);
        if (existente == null) {
            return null;
        }
        if (existente instanceof UsuarioPremium) {
            throw new IllegalArgumentException("Este usuário é premium. Use PUT /usuarios/premium/" + id);
        }
        validarDadosBasicos(novo);
        novo.setId(id);
        novo.setSaldoPontos(existente.getSaldoPontos());
        repositorio.atualizar(novo);
        System.out.println("[AUDITORIA] Usuário atualizado: " + id);
        return repositorio.buscarPorId(id);
    }

    /** Retorna null se o usuário não existir. Também serve para promover um usuário comum a premium. */
    public Usuario atualizarPremium(String id, UsuarioPremium novo) {
        Usuario existente = repositorio.buscarPorId(id);
        if (existente == null) {
            return null;
        }
        validarDadosBasicos(novo);
        validarDadosPremium(novo);
        novo.setId(id);
        novo.setSaldoPontos(existente.getSaldoPontos());
        if (existente instanceof UsuarioPremium) {
            novo.setResgatesRealizadosMes(((UsuarioPremium) existente).getResgatesRealizadosMes());
        } else {
            novo.setResgatesRealizadosMes(0);
        }
        repositorio.atualizar(novo);
        System.out.println("[AUDITORIA] Usuário premium atualizado: " + id);
        return repositorio.buscarPorId(id);
    }

    public boolean remover(String id) {
        boolean removido = repositorio.remover(id);
        if (removido) {
            System.out.println("[AUDITORIA] Usuário removido: " + id);
        }
        return removido;
    }

    public Usuario buscar(String id) {
        return repositorio.buscarPorId(id);
    }

    public ArrayList<Usuario> listar() {
        return repositorio.obterTodos();
    }

    /** Retorna null se o usuário não existir. */
    public Usuario adicionarPontos(String id, Double pontos) {
        Usuario existente = repositorio.buscarPorId(id);
        if (existente == null) {
            return null;
        }
        double valor = Validador.numero(pontos, "pontos", 0.01, 100000);
        repositorio.depositar(id, valor);
        System.out.println("[AUDITORIA] Pontos creditados: usuario=" + id + " pontos=" + valor);
        return repositorio.buscarPorId(id);
    }

    private void validarDadosBasicos(Usuario u) {
        u.setNome(Validador.texto(u.getNome(), "nome", 2, 100));
        u.setEmail(Validador.email(u.getEmail()));
        u.setCpf(Validador.cpf(u.getCpf()));
    }

    private void validarDadosPremium(UsuarioPremium p) {
        Validador.numero(p.getMultiplicadorBonus(), "multiplicadorBonus", 1.0, 5.0);
        Validador.inteiro(p.getLimiteResgatesMensais(), "limiteResgatesMensais", 1, 100);
    }
}
