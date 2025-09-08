package co.com.crediya.model.loanapplication.exceptions;

import co.com.crediya.model.baseexception.BusinessException;

public class NotAllowedLoanTypeException extends BusinessException {
    public NotAllowedLoanTypeException(String typeLoan) {
        super(String.format("Loan's type not allowed: %s", typeLoan), "CONFLICT");
    }

}
