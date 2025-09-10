package co.com.crediya.api.dto;

import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.model.loanapplication.User;
import co.com.crediya.model.loanapplication.gateways.PendingLoanApplication;
import co.com.crediya.model.loanoperation.LoanOperation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Response for a all pending loan application")
public class AllLoanApplicationResponseDto {

    private BigDecimal amount;
    private int term;
    private String email;
    private String name;
    private String loanType;
    private BigDecimal interestRate;
    private String status;
    private BigDecimal baseSalary;
    private BigDecimal monthlyFee;

    public static AllLoanApplicationResponseDto from(PendingLoanApplication loanApplication, User user) {
        BigDecimal montoMensual = LoanOperation.calculateMonthlyFee(
                loanApplication.getAmount(),
                loanApplication.getTerm(),
                BigDecimal.ZERO

        );
        return new AllLoanApplicationResponseDto(
                loanApplication.getAmount(),
                loanApplication.getTerm(),
                loanApplication.getEmail(),
                user.getName(),
                loanApplication.getLoanType(), //traer en consulta tipo prestamo
                loanApplication.getInterestRate(), //traer tipo prestamo en consulta
                loanApplication.getStatus(),
                user.getSalaryBase(),
                montoMensual
        );
    }
}
