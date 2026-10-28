package br.com.urbanlink.controller;

import br.com.urbanlink.dominio.Voucher;
import br.com.urbanlink.dto.ConversaoRequest;
import br.com.urbanlink.dto.StatusRequest;
import br.com.urbanlink.dto.UtilizarRequest;
import br.com.urbanlink.infra.dao.ImpactoAmbientalDAO;
import br.com.urbanlink.infra.dao.TransporteDAO;
import br.com.urbanlink.infra.dao.UsuarioDAO;
import br.com.urbanlink.infra.dao.VoucherDAO;
import br.com.urbanlink.service.VoucherService;
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
@RequestMapping("/vouchers")
public class VoucherController {

    private VoucherService voucherService;

    public VoucherController() {
        this.voucherService = new VoucherService(new VoucherDAO(), new UsuarioDAO(),
                new TransporteDAO(), new ImpactoAmbientalDAO());
    }

    @GetMapping
    public ResponseEntity<Object> obterTodos() {
        try {
            return ResponseEntity.ok(voucherService.listar());
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Object> obterPorUsuario(@PathVariable("usuarioId") String usuarioId) {
        try {
            return ResponseEntity.ok(voucherService.listarPorUsuario(usuarioId));
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> buscar(@PathVariable("codigo") String codigo) {
        try {
            Voucher voucher = voucherService.buscar(codigo);
            if (voucher == null) {
                return RespostaDeErro.naoEncontrado("Voucher não encontrado: " + codigo);
            }
            return ResponseEntity.ok(voucher);
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    /** Converte pontos em voucher. */
    @PostMapping
    public ResponseEntity<Object> converter(@RequestBody ConversaoRequest pedido) {
        try {
            Voucher voucher = voucherService.converter(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(voucher);
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @PutMapping("/{codigo}/utilizar")
    public ResponseEntity<Object> utilizar(@PathVariable("codigo") String codigo, @RequestBody UtilizarRequest pedido) {
        try {
            Voucher voucher = voucherService.utilizar(codigo, pedido.getUsuarioId());
            if (voucher == null) {
                return RespostaDeErro.naoEncontrado("Voucher não encontrado: " + codigo);
            }
            return ResponseEntity.ok(voucher);
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Object> alterarStatus(@PathVariable("codigo") String codigo, @RequestBody StatusRequest pedido) {
        try {
            Voucher voucher = voucherService.alterarStatus(codigo, pedido.getStatus());
            if (voucher == null) {
                return RespostaDeErro.naoEncontrado("Voucher não encontrado: " + codigo);
            }
            return ResponseEntity.ok(voucher);
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> remover(@PathVariable("codigo") String codigo) {
        try {
            if (!voucherService.remover(codigo)) {
                return RespostaDeErro.naoEncontrado("Voucher não encontrado: " + codigo);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return RespostaDeErro.de(e);
        }
    }
}
