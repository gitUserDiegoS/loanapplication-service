package co.com.crediya.model.tokenvalidator.gateways.exception;

import co.com.crediya.model.baseexception.BusinessException;
import co.com.crediya.model.loanapplication.constants.ErrorCodes;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException(String message) {
        super(message, ErrorCodes.UNAUTHORIZED);
    }
}
