package br.com.urbanlink.dominio;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VoucherTest {

    @Test
    void utilizarVoucherAtivoMudaOStatus() {
        Voucher voucher = new Voucher("SUP-1", "USR-001", "TRP-001", 5.0, 50.0, LocalDateTime.now());

        voucher.utilizar();

        assertEquals(StatusVoucher.UTILIZADO, voucher.getStatus());
    }

    @Test
    void naoPodeUtilizarDuasVezes() {
        Voucher voucher = new Voucher("SUP-1", "USR-001", "TRP-001", 5.0, 50.0, LocalDateTime.now());
        voucher.utilizar();

        assertThrows(IllegalArgumentException.class, () -> voucher.utilizar());
    }
}
