package br.com.urbanlink.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConversaoRequest {

    @JsonProperty
    private String usuarioId;

    @JsonProperty
    private String transporteId;

    @JsonProperty
    private Double percentualCobertura;

    public ConversaoRequest() {
    }

    public String getUsuarioId() { return usuarioId; }
    public String getTransporteId() { return transporteId; }
    public Double getPercentualCobertura() { return percentualCobertura; }
}
