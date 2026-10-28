package br.com.urbanlink.dominio;

import br.com.urbanlink.util.Numeros;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransportePublico {

    /** Taxa de conversão: 10 pontos = R$ 1,00. */
    public static final double TAXA_CONVERSAO = 10.0;

    @JsonProperty
    private String id;

    @JsonProperty
    private String nome;

    @JsonProperty
    private String modalidade;

    @JsonProperty
    private double tarifaBase;

    @JsonProperty
    private double co2EvitadoPorUso;

    @JsonProperty
    private boolean integrado;

    public TransportePublico() {
    }

    public TransportePublico(String id, String nome, String modalidade, double tarifaBase, double co2EvitadoPorUso) {
        this.id = id;
        this.nome = nome;
        this.modalidade = modalidade;
        this.tarifaBase = tarifaBase;
        this.co2EvitadoPorUso = co2EvitadoPorUso;
        this.integrado = true;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getModalidade() { return modalidade; }
    public void setModalidade(String modalidade) { this.modalidade = modalidade; }

    public double getTarifaBase() { return tarifaBase; }
    public void setTarifaBase(double tarifaBase) { this.tarifaBase = tarifaBase; }

    public double getCo2EvitadoPorUso() { return co2EvitadoPorUso; }
    public void setCo2EvitadoPorUso(double co2EvitadoPorUso) {
        this.co2EvitadoPorUso = co2EvitadoPorUso;
    }

    public boolean isIntegrado() { return integrado; }
    public void setIntegrado(boolean integrado) { this.integrado = integrado; }

    /** Valor em R$ coberto por um percentual (maior que 0 e até 100) da tarifa. */
    public double calcularValorCredito(double percentualCobertura) {
        validarPercentual(percentualCobertura);
        return Numeros.arredondar(tarifaBase * (percentualCobertura / 100.0), 2);
    }

    /** Pontos necessários para cobrir o percentual informado da tarifa. */
    public double calcularPontosNecessarios(double percentualCobertura) {
        return Numeros.arredondar(calcularValorCredito(percentualCobertura) * TAXA_CONVERSAO, 2);
    }

    private void validarPercentual(double percentual) {
        if (percentual <= 0 || percentual > 100) {
            throw new IllegalArgumentException("Percentual inválido. Use um valor maior que 0 e até 100.");
        }
    }

    @Override
    public String toString() {
        return "=== TRANSPORTE PÚBLICO ===\n"
             + "ID         : " + this.id + "\n"
             + "Nome       : " + this.nome + "\n"
             + "Modalidade : " + this.modalidade + "\n"
             + "Tarifa     : R$ " + String.format("%.2f", this.tarifaBase) + "\n"
             + "CO₂ evitado: " + this.co2EvitadoPorUso + " kg/uso\n"
             + "Integrado  : " + (this.integrado ? "Sim" : "Não");
    }
}
