package co.com.crediya.api;

import co.com.crediya.api.config.LoanApplicationPath;
import co.com.crediya.api.dto.ApplicationResponseDto;
import co.com.crediya.api.dto.LoanApplicationRequestDto;
import co.com.crediya.api.mapper.LoanMapperDto;
import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.model.usersession.UserSession;
import co.com.crediya.usecase.loanapplication.IloanAppicationUseCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@EnableConfigurationProperties(LoanApplicationPath.class)
@WebFluxTest
class RouterRestTest {


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

    @BeforeEach
    void setup() {
        when(loanMapperDto.toModel(any(LoanApplicationRequestDto.class))).thenReturn(loan);
        when(loanApplicationUseCase.saveLoanApplication(any(LoanApplication.class), anyString(), anyString(), any(UserSession.class))).thenReturn(Mono.just(loan));
        when(loanMapperDto.toResponse(any(LoanApplication.class))).thenReturn(responseDto);
    }

    @Test
    void shouldLoadUserPathProperties() {
        assertEquals("/api/v1/solicitud", loanPath.getLoans());
    }


}
