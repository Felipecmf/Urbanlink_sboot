package br.com.urbanlink.controller;

import br.com.urbanlink.dominio.ImpactoAmbiental;
import br.com.urbanlink.infra.dao.ImpactoAmbientalDAO;
import br.com.urbanlink.infra.dao.UsuarioDAO;
import br.com.urbanlink.service.ImpactoAmbientalService;
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
@RequestMapping("/impactos")
public class ImpactoAmbientalController {

    private ImpactoAmbientalService impactoService;

    public ImpactoAmbientalController() {
        this.impactoService = new ImpactoAmbientalService(new ImpactoAmbientalDAO(), new UsuarioDAO());
    }

    @GetMapping
    public ResponseEntity<Object> obterTodos() {
        try {
            return ResponseEntity.ok(impactoService.listar());
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<Object> buscarPorUsuario(@PathVariable("usuarioId") String usuarioId) {
        try {
            ImpactoAmbiental impacto = impactoService.buscarPorUsuario(usuarioId);
            if (impacto == null) {
                return RespostaDeErro.naoEncontrado("Usuário não encontrado: " + usuarioId);
            }
            return ResponseEntity.ok(impacto);
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody ImpactoAmbiental impacto) {
        try {
            ImpactoAmbiental criado = impactoService.adicionar(impacto);
            return ResponseEntity.status(HttpStatus.CREATED).body(criado);
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @PutMapping("/{usuarioId}")
    public ResponseEntity<Object> atualizar(@PathVariable("usuarioId") String usuarioId,
                                            @RequestBody ImpactoAmbiental impacto) {
        try {
            ImpactoAmbiental atualizado = impactoService.atualizar(usuarioId, impacto);
            if (atualizado == null) {
                return RespostaDeErro.naoEncontrado("Usuário não encontrado: " + usuarioId);
            }
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Object> remover(@PathVariable("usuarioId") String usuarioId) {
        try {
            if (!impactoService.remover(usuarioId)) {
                return RespostaDeErro.naoEncontrado("Impacto ambiental não encontrado para o usuário: " + usuarioId);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }
}