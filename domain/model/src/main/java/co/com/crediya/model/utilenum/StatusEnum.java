package co.com.crediya.model.utilenum;

import java.util.Arrays;

public enum StatusEnum {

    APPROVED(2, "Aprobada", "Approved"),

    REJECTED(3, "Rechazada", "Rejected");


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
                .orElseThrow(() -> new IllegalArgumentException("invalid code status " + code));
    }

    public static String translatefromCode(Integer code) {
        return Arrays.stream(values())
                .filter(status -> status.code.equals(code))
                .map(s -> s.translation)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("invalid code status " + code));
    }


}
