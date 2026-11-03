package br.com.urbanlink.dto;

import br.com.urbanlink.dominio.StatusVoucher;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusRequest {

    @JsonProperty
    private StatusVoucher status;

    public StatusRequest() {
    }

    public StatusVoucher getStatus() { return status; }
}
