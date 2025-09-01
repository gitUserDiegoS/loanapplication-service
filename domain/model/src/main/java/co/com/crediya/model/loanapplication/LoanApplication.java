package co.com.crediya.model.loanapplication;

import lombok.*;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanApplication {

    private String idDocument;


    private BigDecimal amount;

    private BigDecimal term;

    private Integer idTypeLoan;

}
