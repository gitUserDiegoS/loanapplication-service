package co.com.crediya.model.passwordencoder.gateways;

import reactor.core.publisher.Mono;

public interface PasswordEncoderRepository {

    Mono<Boolean> matches(String rawPassword, String encodedPassword);
}
