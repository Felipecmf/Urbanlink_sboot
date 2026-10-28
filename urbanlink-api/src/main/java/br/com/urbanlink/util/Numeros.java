package br.com.urbanlink.util;

public class Numeros {

    /** Arredonda para o número de casas informado (ex.: 2 casas para valores em R$ e pontos). */
    public static double arredondar(double valor, int casas) {
        double fator = Math.pow(10, casas);
        return Math.round(valor * fator) / fator;
    }
}
