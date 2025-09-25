package co.com.crediya.model.loannotification;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Loan {

    private BigDecimal amount;

    private BigDecimal rate;

    private Integer term;

    private String status;

}
