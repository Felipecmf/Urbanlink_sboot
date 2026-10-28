package br.com.urbanlink.dominio;

import br.com.urbanlink.util.Numeros;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ImpactoAmbiental {

    /** Uma árvore adulta absorve cerca de 22 kg de CO₂ por ano. */
    public static final double KG_CO2_POR_ARVORE_ANO = 22.0;

    @JsonProperty
    private String usuarioId;

    @JsonProperty
    private double totalCo2Evitado;

    @JsonProperty
    private int totalViagens;

    @JsonProperty
    private double equivalenciaArvores;

    @JsonProperty
    private double pontosInvestidos;

    public ImpactoAmbiental() {
    }

    public ImpactoAmbiental(String usuarioId) {
        this.usuarioId = usuarioId;
        this.totalCo2Evitado = 0.0;
        this.totalViagens = 0;
        this.equivalenciaArvores = 0.0;
        this.pontosInvestidos = 0.0;
    }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public double getTotalCo2Evitado() { return totalCo2Evitado; }
    public void setTotalCo2Evitado(double totalCo2Evitado) { this.totalCo2Evitado = totalCo2Evitado; }

    public int getTotalViagens() { return totalViagens; }
    public void setTotalViagens(int totalViagens) { this.totalViagens = totalViagens; }

    public double getEquivalenciaArvores() { return equivalenciaArvores; }
    public void setEquivalenciaArvores(double equivalenciaArvores) {
        this.equivalenciaArvores = equivalenciaArvores;
    }

    public double getPontosInvestidos() { return pontosInvestidos; }
    public void setPontosInvestidos(double pontosInvestidos) {
        this.pontosInvestidos = pontosInvestidos;
    }

    /** Nível de engajamento calculado a partir do CO₂ acumulado (aparece no JSON). */
    public String getNivel() {
        if (totalCo2Evitado >= 100) {
            return "CAMPEAO SUSTENTAVEL";
        } else if (totalCo2Evitado >= 50) {
            return "ECO WARRIOR";
        } else if (totalCo2Evitado >= 10) {
            return "ECO INICIANTE";
        }
        return "COMECANDO A JORNADA";
    }

    public void registrarViagem(double co2EvitadoViagem, double pontosUsados) {
        this.totalViagens++;
        this.totalCo2Evitado = Numeros.arredondar(this.totalCo2Evitado + co2EvitadoViagem, 3);
        this.pontosInvestidos = Numeros.arredondar(this.pontosInvestidos + pontosUsados, 2);
        this.equivalenciaArvores = Numeros.arredondar(totalCo2Evitado / KG_CO2_POR_ARVORE_ANO, 2);
    }

    @Override
    public String toString() {
        return "====================================\n"
             + "    RELATORIO DE IMPACTO AMBIENTAL  \n"
             + "====================================\n"
             + "Usuario          : " + this.usuarioId + "\n"
             + "Total de viagens : " + this.totalViagens + "\n"
             + "CO2 total evitado: " + String.format("%.3f", this.totalCo2Evitado) + " kg\n"
             + "Equivale a       : " + String.format("%.2f", this.equivalenciaArvores) + " arvores/ano\n"
             + "Pontos investidos: " + this.pontosInvestidos + "\n"
             + "Nivel            : " + getNivel() + "\n"
             + "====================================";
    }
}
