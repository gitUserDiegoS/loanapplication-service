package co.com.crediya.model.tokenvalidator.gateways;

import co.com.crediya.model.usersession.UserSession;
import reactor.core.publisher.Mono;

public interface TokenValidatorRepository {

    Mono<UserSession> validateToken(String token);
}
