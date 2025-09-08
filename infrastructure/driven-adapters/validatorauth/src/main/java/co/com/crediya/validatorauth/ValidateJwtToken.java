package co.com.crediya.validatorauth;

import co.com.crediya.validatorauth.exception.NotValidTokenException;
import co.com.crediya.model.tokenvalidator.gateways.TokenValidatorRepository;
import co.com.crediya.model.usersession.UserSession;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.springframework.security.oauth2.server.resource.BearerTokenErrorCodes.INVALID_TOKEN;

@Slf4j
@Component
public class ValidateJwtToken implements TokenValidatorRepository {

    private final SecretKey secret;

    public ValidateJwtToken(@Value("${JWT_SECRET}") String jwtSecret) {

        log.info("secret " + jwtSecret);
        this.secret = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

    }

    @Override
    public Mono<UserSession> validateToken(String token) {

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token).getBody();


            UserSession session = new UserSession(
                    Long.valueOf(claims.getSubject()),
                    claims.get("email", String.class),
                    claims.get("role", String.class)
            );

            log.info("como llega user get claims rol " + session.getName());
            return Mono.just(session);
        } catch (JwtException e) {
            return Mono.error(new NotValidTokenException(INVALID_TOKEN));
        }
    }
}
