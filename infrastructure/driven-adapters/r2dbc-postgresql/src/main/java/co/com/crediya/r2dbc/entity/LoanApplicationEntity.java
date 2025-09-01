package co.com.crediya.r2dbc.entity;

import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("solicitud")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoanApplicationEntity {

    @Id
    @Column("id_solicitud")
    private Long applicationId;

    @Column("monto")
    private BigDecimal amount;

    @Column("plazo")
    private Integer term;

    private String email;

    @Column("id_estado")
    private Integer status;

    @Column("id_tipo_prestamo")
    private Integer loanType;

}
