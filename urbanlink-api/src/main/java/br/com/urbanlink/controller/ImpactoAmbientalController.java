package br.com.urbanlink.controller;

import br.com.urbanlink.dominio.ImpactoAmbiental;
import br.com.urbanlink.infra.dao.ImpactoAmbientalDAO;
import br.com.urbanlink.infra.dao.UsuarioDAO;
import br.com.urbanlink.service.ImpactoAmbientalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
