package br.com.urbanlink.dominio;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransporteIntegrado extends TransportePublico {

    @JsonProperty
    private String sistemaBilhetagem;

    @JsonProperty
    private int totalViagensProcessadas;

    @JsonProperty
    private boolean aceitaVoucher;

    public TransporteIntegrado() {
        super();
    }

    public TransporteIntegrado(String id, String nome, String modalidade, double tarifaBase, double co2EvitadoPorUso, String sistemaBilhetagem) {
        super(id, nome, modalidade, tarifaBase, co2EvitadoPorUso);
        this.sistemaBilhetagem = sistemaBilhetagem;
        this.totalViagensProcessadas = 0;
        this.aceitaVoucher = true;
    }

    public String getSistemaBilhetagem() { return sistemaBilhetagem; }
    public void setSistemaBilhetagem(String sistemaBilhetagem) {
        this.sistemaBilhetagem = sistemaBilhetagem;
    }

    public int getTotalViagensProcessadas() { return totalViagensProcessadas; }
    public void setTotalViagensProcessadas(int totalViagensProcessadas) {
        this.totalViagensProcessadas = totalViagensProcessadas;
    }

    public boolean isAceitaVoucher() { return aceitaVoucher; }
    public void setAceitaVoucher(boolean aceitaVoucher) { this.aceitaVoucher = aceitaVoucher; }

    public void registrarViagem() {
        this.totalViagensProcessadas++;
    }

    @Override
    public String toString() {
        return super.toString() + "\n"
             + "Sistema    : " + this.sistemaBilhetagem + "\n"
             + "Viagens    : " + this.totalViagensProcessadas + "\n"
             + "Voucher    : " + (this.aceitaVoucher ? "Aceito" : "Não aceito");
    }
}
