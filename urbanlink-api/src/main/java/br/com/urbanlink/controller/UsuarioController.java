package br.com.urbanlink.controller;

import br.com.urbanlink.dominio.Usuario;
import br.com.urbanlink.dominio.UsuarioPremium;
import br.com.urbanlink.dto.PontosRequest;
import br.com.urbanlink.infra.dao.UsuarioDAO;
import br.com.urbanlink.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController() {
        this.usuarioService = new UsuarioService(new UsuarioDAO());
    }

    @GetMapping
    public ResponseEntity<Object> obterTodos() {
        try {
            return ResponseEntity.ok(usuarioService.listar());
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscar(@PathVariable("id") String id) {
        try {
            Usuario usuario = usuarioService.buscar(id);
            if (usuario == null) {
                return RespostaDeErro.naoEncontrado("Usuário não encontrado: " + id);
            }
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody Usuario usuario) {
        try {
            Usuario criado = usuarioService.adicionar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(criado);
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @PostMapping("/premium")
    public ResponseEntity<Object> salvarPremium(@RequestBody UsuarioPremium premium) {
        try {
            UsuarioPremium criado = usuarioService.adicionarPremium(premium);
            return ResponseEntity.status(HttpStatus.CREATED).body(criado);
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable("id") String id, @RequestBody Usuario usuario) {
        try {
            Usuario atualizado = usuarioService.atualizar(id, usuario);
            if (atualizado == null) {
                return RespostaDeErro.naoEncontrado("Usuário não encontrado: " + id);
            }
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @PutMapping("/premium/{id}")
    public ResponseEntity<Object> atualizarPremium(@PathVariable("id") String id, @RequestBody UsuarioPremium premium) {
        try {
            Usuario atualizado = usuarioService.atualizarPremium(id, premium);
            if (atualizado == null) {
                return RespostaDeErro.naoEncontrado("Usuário não encontrado: " + id);
            }
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> remover(@PathVariable("id") String id) {
        try {
            if (!usuarioService.remover(id)) {
                return RespostaDeErro.naoEncontrado("Usuário não encontrado: " + id);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @PostMapping("/{id}/pontos")
    public ResponseEntity<Object> adicionarPontos(@PathVariable("id") String id, @RequestBody PontosRequest pedido) {
        try {
            Usuario usuario = usuarioService.adicionarPontos(id, pedido.getPontos());
            if (usuario == null) {
                return RespostaDeErro.naoEncontrado("Usuário não encontrado: " + id);
            }
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }
}
