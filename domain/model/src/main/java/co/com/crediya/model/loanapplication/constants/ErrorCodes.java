package co.com.crediya.model.loanapplication.constants;

public class ErrorCodes {

    public static final String NOT_FOUND = "NOT_FOUND";

    public static final String CONFLICT = "CONFLICT";

    public static final String UNAUTHORIZED = "UNAUTHORIZED";


    private ErrorCodes() {
        throw new IllegalStateException("Utility class");
    }
}
