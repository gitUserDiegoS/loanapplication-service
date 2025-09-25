package co.com.crediya.model.utilenum;

import java.util.Arrays;

public enum StatusEnum {

    APPROVED(2, "Aprobada", "Approved"),

    REJECTED(3, "Rechazada", "Rejected");


    public static final String INVALID_CODE_STATUS = "invalid code status ";
    private final Integer code;
    private final String description;
    private final String translation;


    StatusEnum(Integer code, String description, String translation) {
        this.code = code;
        this.description = description;
        this.translation = translation;
    }

    public static String fromCode(Integer code) {
        return Arrays.stream(values())
                .filter(status -> status.code.equals(code))
                .map(s -> s.description)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(INVALID_CODE_STATUS + code));
    }

    public static String translatefromCode(Integer code) {
        return Arrays.stream(values())
                .filter(status -> status.code.equals(code))
                .map(s -> s.translation)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(INVALID_CODE_STATUS + code));
    }


    public static String translatefromText(String code) {
        return Arrays.stream(values())
                .filter(status -> status.translation.equalsIgnoreCase(code))
                .map(s -> s.description)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(INVALID_CODE_STATUS + code));
    }

}
