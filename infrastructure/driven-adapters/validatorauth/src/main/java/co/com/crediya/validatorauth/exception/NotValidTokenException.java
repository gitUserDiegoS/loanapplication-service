package co.com.crediya.validatorauth.exception;

import co.com.crediya.model.baseexception.BusinessException;

public class NotValidTokenException extends BusinessException {
    public NotValidTokenException(String message) {
        super(message, "CONFLICT");
    }
}
