package co.com.crediya.r2dbc.dto;

import java.math.BigDecimal;

public record AutomaticLoanDto(
        BigDecimal amount,
        Integer term,
        String email,
        String name,
        BigDecimal rate,
        String status
) {}