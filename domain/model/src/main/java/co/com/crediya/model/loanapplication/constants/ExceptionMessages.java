package co.com.crediya.model.loanapplication.constants;


public class ExceptionMessages {

    public static final String NOT_ALLOWED_USER = "Cannot create a loan application for another user";

    private ExceptionMessages() {
        throw new IllegalStateException("Utility class");
    }
}
