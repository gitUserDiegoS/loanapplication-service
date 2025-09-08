package co.com.crediya.validatorauth;

import co.com.crediya.model.loanapplication.constants.ExceptionMessages;
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

    private static final String TYPE_TOKEN = "Bearer ";

    private static final String TYPE_ROLE = "ROLE_";

    public JwtAuthenticationFilter(TokenValidatorRepository tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        log.info("Init validation for header Authorization");

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(TYPE_TOKEN)) {
            String token = authHeader.substring(7);

            return tokenProvider.validateToken(token)
                    .flatMap(userSession -> {
                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                userSession,
                                null,
                                List.of(new SimpleGrantedAuthority(TYPE_ROLE + userSession.getName()))
                        );
                        return chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                    })
                    .doOnNext(auth -> log.info("Authorized rol"))
                    .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, ExceptionMessages.INVALID_TOKEN)));
        }

        return chain.filter(exchange);
    }
}
