package co.com.crediya.model.usersession;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserSession {

    private final Long userId;

    private final String email;

    private final String name;

}
