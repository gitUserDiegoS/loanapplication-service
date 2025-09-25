package co.com.crediya.model.loannotification;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanNotificationRequest {

    private Long idApplication;

    private BigDecimal salaryBase;

    private BigDecimal amount;

    private BigDecimal rate;

    private Integer term;

    private List<Loan> loans;

}
