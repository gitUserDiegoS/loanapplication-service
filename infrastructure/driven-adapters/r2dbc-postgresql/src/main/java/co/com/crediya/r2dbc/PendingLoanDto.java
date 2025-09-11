package co.com.crediya.r2dbc;

import java.math.BigDecimal;

public record PendingLoanDto (
        BigDecimal monto,
        Integer plazo,
        String email,
        String nombre,
        BigDecimal interes,
        String estado
) {}