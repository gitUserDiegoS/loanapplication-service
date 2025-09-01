package co.com.crediya.usecase.loanapplication.exception;

public class NotAllowedLoanTypeException extends RuntimeException {
    public NotAllowedLoanTypeException(String message) {
        super(message);
    }

}
