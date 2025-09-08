package co.com.crediya.model.loanapplication.exceptions;

import co.com.crediya.model.baseexception.BusinessException;
import co.com.crediya.model.loanapplication.constants.ErrorCodes;

public class NotAllowedLoanTypeException extends BusinessException {
    public NotAllowedLoanTypeException(String message) {
        super(message, ErrorCodes.CONFLICT);
    }

}
