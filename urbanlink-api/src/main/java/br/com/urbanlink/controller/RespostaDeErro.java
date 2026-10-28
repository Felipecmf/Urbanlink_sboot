package br.com.urbanlink.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/** Monta as respostas de erro dos controllers (400 para dados inválidos, 500 para falhas inesperadas). */
class RespostaDeErro {

    static ResponseEntity<Object> de(RuntimeException e) {
        if (e instanceof IllegalArgumentException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        System.out.println(e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno ao processar a requisição. Tente novamente.");
    }

    static ResponseEntity<Object> naoEncontrado(String mensagem) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem);
    }
}
