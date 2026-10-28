package br.com.urbanlink.dominio;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Voucher {

    @JsonProperty
    private String codigo;

    @JsonProperty
    private String usuarioId;

    @JsonProperty
    private String transporteId;

    @JsonProperty
    private double valorCredito;

    @JsonProperty
    private double pontosUsados;

    @JsonProperty
    private StatusVoucher status;

    @JsonProperty
    private LocalDateTime dataGeracao;

    @JsonProperty
    private boolean antifraude;

    public Voucher() {
    }

    public Voucher(String codigo, String usuarioId, String transporteId,
                   double valorCredito, double pontosUsados, LocalDateTime dataGeracao) {
        this.codigo = codigo;
        this.usuarioId = usuarioId;
        this.transporteId = transporteId;
        this.valorCredito = valorCredito;
        this.pontosUsados = pontosUsados;
        this.dataGeracao = dataGeracao;
        this.status = StatusVoucher.ATIVO;
        this.antifraude = true;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getTransporteId() { return transporteId; }
    public void setTransporteId(String transporteId) { this.transporteId = transporteId; }

    public double getValorCredito() { return valorCredito; }
    public void setValorCredito(double valorCredito) { this.valorCredito = valorCredito; }

    public double getPontosUsados() { return pontosUsados; }
    public void setPontosUsados(double pontosUsados) { this.pontosUsados = pontosUsados; }

    public StatusVoucher getStatus() { return status; }
    public void setStatus(StatusVoucher status) { this.status = status; }

    public LocalDateTime getDataGeracao() { return dataGeracao; }
    public void setDataGeracao(LocalDateTime dataGeracao) { this.dataGeracao = dataGeracao; }

    public boolean isAntifraude() { return antifraude; }
    public void setAntifraude(boolean antifraude) { this.antifraude = antifraude; }

    /** Marca o voucher como utilizado, se estiver ATIVO e aprovado no antifraude. */
    public void utilizar() {
        if (!antifraude) {
            throw new IllegalArgumentException("Voucher " + codigo + " bloqueado por suspeita de fraude.");
        }
        if (status != StatusVoucher.ATIVO) {
            throw new IllegalArgumentException("Voucher " + codigo + " não está ativo. Status atual: " + status);
        }
        this.status = StatusVoucher.UTILIZADO;
    }

    @Override
    public String toString() {
        return "==============================\n"
             + "   VOUCHER URBANLINK          \n"
             + "==============================\n"
             + "Codigo  : " + this.codigo + "\n"
             + "Credito : R$ " + String.format("%.2f", this.valorCredito) + "\n"
             + "Pontos  : " + this.pontosUsados + "\n"
             + "Status  : " + this.status + "\n"
             + "Data    : " + this.dataGeracao + "\n"
             + "Fraude  : " + (this.antifraude ? "OK" : "SUSPEITO") + "\n"
             + "==============================";
    }
}
