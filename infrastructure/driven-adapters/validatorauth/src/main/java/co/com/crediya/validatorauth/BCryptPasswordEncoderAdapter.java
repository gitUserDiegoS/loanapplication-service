package co.com.crediya.validatorauth;


import co.com.crediya.model.passwordencoder.gateways.PasswordEncoderRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Getter
@Setter
@Component
public class BCryptPasswordEncoderAdapter implements PasswordEncoderRepository {

    private final BCryptPasswordEncoder delegate;

    public BCryptPasswordEncoderAdapter() {
        this.delegate = new BCryptPasswordEncoder();
    }

    @Override
    public Mono<Boolean> matches(String rawPassword, String encodedPassword) {
        return Mono.fromCallable(() -> delegate.matches(rawPassword, encodedPassword))
                .doOnNext(matches -> log.info("Matche"));

    }
}
