package co.com.crediya.consumer;

import co.com.crediya.consumer.dto.UserFoundResponseDto;
import co.com.crediya.model.loanapplication.User;
import co.com.crediya.model.loanapplication.gateways.UserGatewayRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestConsumer implements UserGatewayRepository {
    private final WebClient client;


    @CircuitBreaker(name = "listenGetUserByDocumentId")
    @Override
    public Mono<User> findUserByIdDocument(String email) {
        log.info("Search user by idDocument {}", email);
        return client
                .get()
                .uri("/api/v1/usuarios/{idDocument}", email)
                .retrieve()
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
