package br.com.urbanlink.util;

import java.util.UUID;

/** Validações de entrada usadas pela camada service. Dados inválidos geram IllegalArgumentException. */
public class Validador {

    public static String texto(String valor, String campo, int min, int max) {
        String t = valor == null ? "" : valor.trim();
        if (t.length() < min) {
            throw new IllegalArgumentException("O campo '" + campo + "' é obrigatório (mínimo de " + min + " caracteres).");
        }
        if (t.length() > max) {
            throw new IllegalArgumentException("O campo '" + campo + "' excede " + max + " caracteres.");
        }
        return t;
    }

    public static String email(String valor) {
        String t = texto(valor, "email", 5, 120).toLowerCase();
        int arroba = t.indexOf("@");
        boolean valido = arroba > 0
                && arroba == t.lastIndexOf("@")
                && t.indexOf(".", arroba) > arroba + 1
                && !t.endsWith(".")
                && !t.contains(" ");
        if (!valido) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        return t;
    }

    /** Retorna o CPF somente com dígitos, depois de validar os dígitos verificadores. */
    public static String cpf(String valor) {
        String digitos = valor == null ? "" : valor.replaceAll("[^0-9]", "");
        if (!cpfValido(digitos)) {
            throw new IllegalArgumentException("CPF inválido.");
        }
        return digitos;
    }

    public static boolean cpfValido(String d) {
        if (d.length() != 11) {
            return false;
        }
        boolean todosIguais = true;
        for (int i = 1; i < 11; i++) {
            if (d.charAt(i) != d.charAt(0)) {
                todosIguais = false;
            }
        }
        if (todosIguais) {
            return false;
        }
        for (int t = 9; t <= 10; t++) {
            int soma = 0;
            for (int i = 0; i < t; i++) {
                soma += (d.charAt(i) - '0') * (t + 1 - i);
            }
            int digito = (soma * 10) % 11;
            if (digito == 10) {
                digito = 0;
            }
            if (digito != d.charAt(t) - '0') {
                return false;
            }
        }
        return true;
    }

    /** Usa o id informado (validado) ou gera um novo com o prefixo, ex.: USR-1A2B3C4D. */
    public static String id(String valor, String prefixo) {
        if (valor == null || valor.trim().isEmpty()) {
            return gerarId(prefixo);
        }
        String t = valor.trim();
        if (t.length() < 3 || t.length() > 20 || !t.matches("[A-Za-z0-9_-]+")) {
            throw new IllegalArgumentException("O id deve ter de 3 a 20 caracteres (letras, números, '-' ou '_').");
        }
        return t;
    }

    public static String gerarId(String prefixo) {
        return prefixo + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    public static double numero(Double valor, String campo, double min, double max) {
        if (valor == null) {
            throw new IllegalArgumentException("O campo '" + campo + "' é obrigatório.");
        }
        if (valor.isNaN() || valor.isInfinite() || valor < min || valor > max) {
            throw new IllegalArgumentException("O campo '" + campo + "' deve estar entre " + min + " e " + max + ".");
        }
        return valor;
    }

    public static int inteiro(Integer valor, String campo, int min, int max) {
        if (valor == null) {
            throw new IllegalArgumentException("O campo '" + campo + "' é obrigatório.");
        }
        if (valor < min || valor > max) {
            throw new IllegalArgumentException("O campo '" + campo + "' deve estar entre " + min + " e " + max + ".");
        }
        return valor;
    }
}
