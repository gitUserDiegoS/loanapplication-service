package co.com.crediya.model.loanapplication.exceptions;

import co.com.crediya.model.baseexception.BusinessException;
import co.com.crediya.model.loanapplication.constants.ErrorCodes;

public class CreationNotAllowedException extends BusinessException {
    public CreationNotAllowedException(String message) {
        super(message, ErrorCodes.CONFLICT);
    }
}
