package co.com.crediya.api.dto;

import java.math.BigDecimal;

public record AllLoanApplicationResponseDto(Long idSolicitud, BigDecimal monto, Integer plazo, String email,
                                            String nombre, String tipoPrestamo, BigDecimal tasaInteres,
                                            String estadoSolicitud, BigDecimal salarioBase,
                                            BigDecimal montoMensualSolicitud) {


}
