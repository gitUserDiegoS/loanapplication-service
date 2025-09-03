package co.com.crediya.model.loanapplication.constants;


public class ModelExceptionMessages {

    public static final String INVALID_EMAIL = "Email must not be null or empty";

    public static final String INVALID_FORMAT_EMAIL = "Invalid format email: %s";

    public static final String INVALID_NAME = "Name must not be null or empty";

    public static final String INVALID_LAST_NAME = "Last name must not be null or empty";

    public static final String INVALID_SALARY = "Salary must not be null";

    public static final String INVALID_SALARY_RANGE = "Salary base must be in a range between 0 and 15000000: %s";

    private ModelExceptionMessages() {
        throw new IllegalStateException("Utility class");
    }
}
