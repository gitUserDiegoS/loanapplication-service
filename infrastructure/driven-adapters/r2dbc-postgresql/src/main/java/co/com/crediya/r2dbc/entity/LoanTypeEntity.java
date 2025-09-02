package co.com.crediya.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("tipo_prestamo")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoanTypeEntity {

    @Id
    @Column("id_tipo_prestamo")
    private Long IdloanType;

    @Column("nombre")
    private String name;

    @Column("monto_minimo")
    private BigDecimal minAmount;

    @Column("monto_maximo")
    private BigDecimal maxAmount;

    @Column("tasa_interes")
    private BigDecimal interestRate;

    @Column("validacion_automatica")
    private boolean automaticValidation;

}
