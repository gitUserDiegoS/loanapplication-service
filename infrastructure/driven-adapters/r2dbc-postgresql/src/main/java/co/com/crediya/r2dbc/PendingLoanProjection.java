package co.com.crediya.r2dbc;

import java.math.BigDecimal;

public interface PendingLoanProjection {
    BigDecimal getMonto();

    Integer getPlazo();

    String getEmail();

    String getNombre();        // tipo.nombre

    BigDecimal getTasaInteres();

    String getEstado();        // estado.nombre
}