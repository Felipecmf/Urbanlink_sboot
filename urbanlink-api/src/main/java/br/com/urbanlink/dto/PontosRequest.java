package br.com.urbanlink.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Corpo de POST /usuarios/{id}/pontos. */
public class PontosRequest {

    @JsonProperty
    private Double pontos;

    public PontosRequest() {
    }

    public Double getPontos() { return pontos; }
}
