package co.com.crediya.api.config;

import co.com.crediya.api.Handler;
import co.com.crediya.api.RouterRest;
import co.com.crediya.api.dto.ApplicationResponseDto;
import co.com.crediya.api.dto.LoanApplicationRequestDto;
import co.com.crediya.api.mapper.LoanMapperDto;
import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.usecase.loanapplication.IloanAppicationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, Handler.class, LoanApplicationPath.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private IloanAppicationUseCase loanApplicationUseCase;

    @MockitoBean
    private LoanMapperDto loanMapperDto;

    @Autowired
    private LoanApplicationPath loanPath;

    private final LoanApplication loan = LoanApplication.builder()
            .idApplication(1L)
            .amount(BigDecimal.TEN)
            .term(12)
            .email("email@test.com")
            .status(1)
            .loanType(1)
            .build();

    private final ApplicationResponseDto responseDto = ApplicationResponseDto.builder()
            .idApplication(1L)
            .build();

    private final LoanApplicationRequestDto request = LoanApplicationRequestDto.builder()
            .idDocument("123")
            .amount(BigDecimal.TEN)
            .term(12)
            .build();

    @BeforeEach
    void setup() {
        when(loanMapperDto.toModel(any(LoanApplicationRequestDto.class))).thenReturn(loan);
        when(loanApplicationUseCase.saveLoanApplication(any(LoanApplication.class), anyString())).thenReturn(Mono.just(loan));
        when(loanMapperDto.toResponse(any(LoanApplication.class))).thenReturn(responseDto);
    }

    @Test
    void corsConfigurationShouldAllowOrigins() {
        webTestClient.post()
                .uri(loanPath.getLoans())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

}