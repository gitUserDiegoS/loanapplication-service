package co.com.crediya.r2dbc;

import java.math.BigDecimal;

public record PendingLoanDto (
        BigDecimal amount,
        Integer term,
        String email,
        String name,
        BigDecimal rate,
        String status
) {}