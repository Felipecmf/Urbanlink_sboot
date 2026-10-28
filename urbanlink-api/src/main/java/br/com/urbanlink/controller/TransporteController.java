package br.com.urbanlink.controller;

import br.com.urbanlink.dominio.TransporteIntegrado;
import br.com.urbanlink.dominio.TransportePublico;
import br.com.urbanlink.infra.dao.TransporteDAO;
import br.com.urbanlink.service.TransporteService;
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
@RequestMapping("/transportes")
public class TransporteController {

    private TransporteService transporteService;

    public TransporteController() {
        this.transporteService = new TransporteService(new TransporteDAO());
    }

    @GetMapping
    public ResponseEntity<Object> obterTodos() {
        try {
            return ResponseEntity.ok(transporteService.listar());
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscar(@PathVariable("id") String id) {
        try {
            TransportePublico transporte = transporteService.buscar(id);
            if (transporte == null) {
                return RespostaDeErro.naoEncontrado("Transporte não encontrado: " + id);
            }
            return ResponseEntity.ok(transporte);
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody TransportePublico transporte) {
        try {
            TransportePublico criado = transporteService.adicionar(transporte);
            return ResponseEntity.status(HttpStatus.CREATED).body(criado);
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @PostMapping("/integrados")
    public ResponseEntity<Object> salvarIntegrado(@RequestBody TransporteIntegrado transporte) {
        try {
            TransporteIntegrado criado = transporteService.adicionarIntegrado(transporte);
            return ResponseEntity.status(HttpStatus.CREATED).body(criado);
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable("id") String id, @RequestBody TransportePublico transporte) {
        try {
            TransportePublico atualizado = transporteService.atualizar(id, transporte);
            if (atualizado == null) {
                return RespostaDeErro.naoEncontrado("Transporte não encontrado: " + id);
            }
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @PutMapping("/integrados/{id}")
    public ResponseEntity<Object> atualizarIntegrado(@PathVariable("id") String id, @RequestBody TransporteIntegrado transporte) {
        try {
            TransportePublico atualizado = transporteService.atualizarIntegrado(id, transporte);
            if (atualizado == null) {
                return RespostaDeErro.naoEncontrado("Transporte não encontrado: " + id);
            }
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> remover(@PathVariable("id") String id) {
        try {
            if (!transporteService.remover(id)) {
                return RespostaDeErro.naoEncontrado("Transporte não encontrado: " + id);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }
}
