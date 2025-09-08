package co.com.crediya.model.loanapplication.constants;


public class ExceptionMessages {

    public static final String NOT_ALLOWED_USER = "Cannot create a loan application for another user";

    public static final String INVALID_TOKEN = "Not valid token";

    public static final String NOT_ALLOWED_LOAN_TYPE = "Loan's type not allowed: %s";

    public static final String USER_NOT_FOUND = "User not found by id: %s";

    private ExceptionMessages() {
        throw new IllegalStateException("Utility class");
    }
}
