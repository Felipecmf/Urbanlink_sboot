package br.com.urbanlink.dominio;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Usuario {

    @JsonProperty
    private String id;

    @JsonProperty
    private String nome;

    @JsonProperty
    private String email;

    @JsonProperty
    private String cpf;

    @JsonProperty
    private double saldoPontos;

    /** Construtor vazio: usado pelo Jackson e pelo DAO ao montar o objeto a partir do banco. */
    public Usuario() {
    }

    public Usuario(String id, String nome, String email, String cpf) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.saldoPontos = 0.0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public double getSaldoPontos() { return saldoPontos; }
    public void setSaldoPontos(double saldoPontos) { this.saldoPontos = saldoPontos; }

    /** Tipo exposto no JSON ("COMUM" ou "PREMIUM"); a subclasse sobrescreve (polimorfismo). */
    public String getTipo() {
        return "COMUM";
    }

    public void adicionarPontos(double pontos) {
        if (pontos <= 0) {
            throw new IllegalArgumentException("Valor de pontos inválido para adicionar.");
        }
        this.saldoPontos += pontos;
    }

    public void reduzirPontos(double pontos) {
        if (pontos <= 0) {
            throw new IllegalArgumentException("Valor de pontos inválido para deduzir.");
        }
        if (this.saldoPontos < pontos) {
            throw new IllegalArgumentException("Saldo insuficiente. Saldo: " + saldoPontos + " | Necessário: " + pontos);
        }
        this.saldoPontos -= pontos;
    }

    @Override
    public String toString() {
        return "=== PERFIL DO USUÁRIO ===\n"
             + "ID     : " + this.id + "\n"
             + "Nome   : " + this.nome + "\n"
             + "Email  : " + this.email + "\n"
             + "CPF    : " + this.cpf + "\n"
             + "Pontos : " + this.saldoPontos;
    }
}
