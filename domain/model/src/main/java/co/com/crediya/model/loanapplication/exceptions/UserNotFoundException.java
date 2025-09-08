package co.com.crediya.model.loanapplication.exceptions;

import co.com.crediya.model.baseexception.BusinessException;
import co.com.crediya.model.loanapplication.constants.ErrorCodes;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String message) {
        super(message, ErrorCodes.NOT_FOUND);
    }
}
