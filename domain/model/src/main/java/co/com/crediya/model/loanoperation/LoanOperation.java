package co.com.crediya.model.loanoperation;

import lombok.Builder;

import lombok.Getter;

import lombok.Setter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Getter
@Setter
@Builder(toBuilder = true)
public class LoanOperation {

    public static BigDecimal calculateMonthlyFee(BigDecimal amount, int term, BigDecimal interestRate) {
        if (term <= 0) return BigDecimal.ZERO;

        if (interestRate == null || interestRate.compareTo(BigDecimal.ZERO) == 0) {
            return amount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_UP);
        }

        BigDecimal monthlyFee = interestRate.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

        BigDecimal divisor = BigDecimal.ONE.subtract(
                BigDecimal.ONE.add(monthlyFee).pow(-term, MathContext.DECIMAL64)
        );

        return amount.multiply(monthlyFee).divide(divisor, 2, RoundingMode.HALF_UP);
    }

    private LoanOperation() {
        throw new IllegalStateException("Utility class");
    }

}
