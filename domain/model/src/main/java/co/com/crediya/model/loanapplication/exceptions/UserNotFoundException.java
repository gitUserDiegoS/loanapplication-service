package co.com.crediya.model.loanapplication.exceptions;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String idDocument) {

        super(String.format("User not found by id: %s", idDocument), "USER_NOT_FOUND");
    }
}
