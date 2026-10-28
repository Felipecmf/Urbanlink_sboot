package br.com.urbanlink.dominio;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsuarioPremium extends Usuario {

    @JsonProperty
    private double multiplicadorBonus;

    @JsonProperty
    private int limiteResgatesMensais;

    @JsonProperty
    private int resgatesRealizadosMes;

    public UsuarioPremium() {
        super();
    }

    public UsuarioPremium(String id, String nome, String email, String cpf, double multiplicadorBonus, int limiteResgatesMensais) {
        super(id, nome, email, cpf);
        this.multiplicadorBonus = multiplicadorBonus;
        this.limiteResgatesMensais = limiteResgatesMensais;
        this.resgatesRealizadosMes = 0;
    }

    public double getMultiplicadorBonus() { return multiplicadorBonus; }
    public void setMultiplicadorBonus(double multiplicadorBonus) {
        this.multiplicadorBonus = multiplicadorBonus;
    }

    public int getLimiteResgatesMensais() { return limiteResgatesMensais; }
    public void setLimiteResgatesMensais(int limiteResgatesMensais) {
        this.limiteResgatesMensais = limiteResgatesMensais;
    }

    public int getResgatesRealizadosMes() { return resgatesRealizadosMes; }
    public void setResgatesRealizadosMes(int resgatesRealizadosMes) {
        this.resgatesRealizadosMes = resgatesRealizadosMes;
    }

    @Override
    public String getTipo() {
        return "PREMIUM";
    }

    public boolean podeResgatar() {
        return resgatesRealizadosMes < limiteResgatesMensais;
    }

    public void registrarResgate() {
        resgatesRealizadosMes++;
    }

    @Override
    public String toString() {
        return super.toString() + "\n"
             + "Tipo   : PREMIUM\n"
             + "Bônus  : x" + this.multiplicadorBonus + "\n"
             + "Resgates no mês: " + this.resgatesRealizadosMes + "/" + this.limiteResgatesMensais;
    }
}
