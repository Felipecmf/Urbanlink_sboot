package br.com.urbanlink.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Corpo de PUT /vouchers/{codigo}/utilizar. */
public class UtilizarRequest {

    @JsonProperty
    private String usuarioId;

    public UtilizarRequest() {
    }

    public String getUsuarioId() { return usuarioId; }
}
