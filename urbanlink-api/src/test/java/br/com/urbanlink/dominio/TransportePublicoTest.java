package br.com.urbanlink.dominio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransportePublicoTest {

    @Test
    void calcularPontosParaTarifaCompleta() {
        TransportePublico metro = new TransportePublico("TRP-001", "Metrô SP", "Metrô", 5.00, 2.5);

        assertEquals(50.0, metro.calcularPontosNecessarios(100));
    }

    @Test
    void calcularValorDeMetadeDaTarifa() {
        TransportePublico metro = new TransportePublico("TRP-001", "Metrô SP", "Metrô", 5.00, 2.5);

        assertEquals(2.5, metro.calcularValorCredito(50));
    }

    @Test
    void percentualInvalidoDeveLancarExcecao() {
        TransportePublico metro = new TransportePublico("TRP-001", "Metrô SP", "Metrô", 5.00, 2.5);

        assertThrows(IllegalArgumentException.class, () -> metro.calcularPontosNecessarios(0));
        assertThrows(IllegalArgumentException.class, () -> metro.calcularPontosNecessarios(101));
    }
}
