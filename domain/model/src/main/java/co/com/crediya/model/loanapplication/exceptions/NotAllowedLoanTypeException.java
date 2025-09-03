package co.com.crediya.model.loanapplication.exceptions;

public class NotAllowedLoanTypeException extends BusinessException {
    public NotAllowedLoanTypeException(String typeLoan) {
        super(String.format("Loan's type not allowed: %s", typeLoan), "CONFLICT");
    }

}
