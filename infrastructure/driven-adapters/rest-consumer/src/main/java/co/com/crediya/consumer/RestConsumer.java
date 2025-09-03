package co.com.crediya.consumer;

import co.com.crediya.consumer.dto.UserFoundResponseDto;
import co.com.crediya.model.loanapplication.User;
import co.com.crediya.model.loanapplication.exceptions.UserNotFoundException;
import co.com.crediya.model.loanapplication.gateways.UserGatewayRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestConsumer implements UserGatewayRepository {
    private final WebClient client;


    @CircuitBreaker(name = "listenGetUserByDocumentId")
    @Override
    public Mono<User> findUserByIdDocument(String idDocument) {
        log.info("Search user by idDocument {}", idDocument);
        return client
                .get()
                .uri("/api/v1/usuarios/{idDocument}", idDocument)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response -> Mono.error(new UserNotFoundException(idDocument)))
                .bodyToMono(UserFoundResponseDto.class)
                .map(this::toDomain);
    }

    private User toDomain(UserFoundResponseDto dto) {
        return User.builder()
                .idDocument(dto.getIdDocument())
                .email(dto.getEmail())
                .build();
    }

}
