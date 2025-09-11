package co.com.crediya.consumer;

import co.com.crediya.consumer.dto.UserFoundResponseDto;
import co.com.crediya.model.loanapplication.User;
import co.com.crediya.model.loanapplication.constants.ExceptionMessages;
import co.com.crediya.model.loanapplication.exceptions.UserNotFoundException;
import co.com.crediya.model.loanapplication.gateways.UserGatewayRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestConsumer implements UserGatewayRepository {

    public static final String GET_USER_BY_ID = "/api/v1/usuarios/{idDocument}";

    private final WebClient client;


    @CircuitBreaker(name = "listenGetUserByDocumentId")
    @Override
    public Mono<User> findUserByIdDocument(String idDocument, String token) {
        log.info("Consuming Authentication service client, Search user by idDocument {}", idDocument);
        return client
                .get()
                .uri(GET_USER_BY_ID, idDocument)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response -> Mono.error(new UserNotFoundException(String.format(ExceptionMessages.USER_NOT_FOUND, idDocument))))
                .bodyToMono(UserFoundResponseDto.class)
                .map(this::toDomain)
                .doOnError(err -> log.error("Error in client RestConsumer-->findUserByIdDocument {} ", err.getMessage(), err));
    }

    @Override
    public Flux<User> getUsersByEmailBatch(List<String> emails, String token) {
        log.info("Diego log-->" + token);

        log.info("Diego emails-->" + emails);


        return client.post()
                .uri("/api/v1/usuarios/emails")
                .header(HttpHeaders.AUTHORIZATION, token)
                .bodyValue(emails)
                .retrieve()
                .bodyToFlux(UserFoundResponseDto.class)
                .map(this::toDomain)
                .doOnError(err -> log.error("Error in client RestConsumer-->findUserByIdDocument {} ", err.getMessage(), err));

    }

    private User toDomain(UserFoundResponseDto dto) {
        return User.builder()
                .idDocument(dto.getIdDocument())
                .email(dto.getEmail())
                .name(dto.getName() + " " + dto.getLastname())
                .lastname(dto.getName())
                .salaryBase(dto.getSalaryBase())
                .build();
    }

}
