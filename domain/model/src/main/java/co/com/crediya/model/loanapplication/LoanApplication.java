package co.com.crediya.model.loanapplication;

import lombok.*;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanApplication {


    private Long idApplication;

    private BigDecimal amount;

    private Integer term;

    private String email;

    private Integer status;

    private Integer loanType;


}
