package br.com.urbanlink.dominio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UsuarioTest {

    @Test
    void adicionarEReduzirPontos() {
        Usuario ana = new Usuario("USR-001", "Ana Souza", "ana@email.com", "52998224725");

        ana.adicionarPontos(100);
        ana.reduzirPontos(40);

        assertEquals(60.0, ana.getSaldoPontos());
    }

    @Test
    void saldoInsuficienteDeveLancarExcecao() {
        Usuario ana = new Usuario("USR-001", "Ana Souza", "ana@email.com", "52998224725");

        assertThrows(IllegalArgumentException.class, () -> ana.reduzirPontos(10));
    }

    @Test
    void premiumUsaToStringDaSuperclasse() {
        UsuarioPremium bia = new UsuarioPremium("USR-003", "Beatriz", "bia@email.com", "52998224725", 1.5, 10);

        assertEquals(true, bia.toString().contains("PERFIL DO USUÁRIO"));
        assertEquals(true, bia.toString().contains("PREMIUM"));
    }
}
