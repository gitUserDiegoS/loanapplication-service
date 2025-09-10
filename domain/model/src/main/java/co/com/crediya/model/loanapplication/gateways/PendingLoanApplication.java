package co.com.crediya.model.loanapplication.gateways;

import co.com.crediya.model.loanapplication.User;
import co.com.crediya.model.loanoperation.LoanOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PendingLoanApplication {


    private BigDecimal amount;
    private int term;
    private String email;

    private String name;//revisar


    private String loanType;
    private BigDecimal interestRate;

    private String status;//revisar
    private BigDecimal baseSalary;//revisar

    private BigDecimal monthlyFee;

}
