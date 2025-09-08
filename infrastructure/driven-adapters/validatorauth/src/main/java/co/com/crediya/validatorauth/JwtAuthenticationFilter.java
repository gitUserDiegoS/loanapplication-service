package co.com.crediya.validatorauth;

import co.com.crediya.model.tokenvalidator.gateways.TokenValidatorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationFilter implements WebFilter {

    private final TokenValidatorRepository tokenProvider;

    public JwtAuthenticationFilter(TokenValidatorRepository tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        log.info("path Diego " + path);

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.info("path Diego token IN JWAT AUTHENTICATION in filter" + token);

            return tokenProvider.validateToken(token)
                    .flatMap(userSession -> {
                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                userSession,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + userSession.getName()))
                        );
                        return chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                    })
                    .doOnNext(auth -> log.info("authorized rol " + auth))
                    .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token")));
        }

        return chain.filter(exchange);
    }
}
