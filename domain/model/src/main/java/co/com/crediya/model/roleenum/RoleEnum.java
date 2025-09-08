package co.com.crediya.model.roleenum;

import java.util.Arrays;

public enum RoleEnum {

    CLIENT(1L, "CLIENT"),

    ADMIN(2L, "ADMIN"),

    ADVISOR(3L, "ADVISOR");

    private final Long id;
    private final String name;


    RoleEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static RoleEnum fromId(Long id) {
        return Arrays.stream(values())
                .filter(role -> role.id.equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("invalid role " + id));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
