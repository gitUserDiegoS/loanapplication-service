package co.com.crediya.model.loanapplication.gateways;

import co.com.crediya.model.loanapplication.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserGatewayRepository {

    Mono<User> findUserByIdDocument(String idDocument, String token);

    Flux<User> getUsersByEmailBatch(List<String> email, String token);

}
