package br.com.urbanlink.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidadorTest {

    @Test
    void cpfValidoRetornaSomenteDigitos() {
        assertEquals("52998224725", Validador.cpf("529.982.247-25"));
    }

    @Test
    void cpfInvalidoDeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () -> Validador.cpf("111.111.111-11"));
    }

    @Test
    void emailInvalidoDeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () -> Validador.email("ana.email.com"));
    }
}
