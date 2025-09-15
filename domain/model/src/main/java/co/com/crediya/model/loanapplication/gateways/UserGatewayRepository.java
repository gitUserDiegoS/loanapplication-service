package co.com.crediya.model.loanapplication.gateways;

import co.com.crediya.model.loanapplication.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserGatewayRepository {

    Mono<User> findUserByIdDocument(String idDocument, String token);

    Flux<User> getUsersByEmailBatch(Flux<String> email, String token);

}
