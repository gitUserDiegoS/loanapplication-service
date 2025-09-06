package co.com.crediya.r2dbc;

import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.r2dbc.entity.LoanApplicationEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;

import org.springframework.transaction.reactive.TransactionalOperator;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanApplicationRepositoryAdapterTest {

    @InjectMocks
    LoanApplicationRepositoryAdapter repositoryAdapter;

    @Mock
    LoanApplicationReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Mock
    TransactionalOperator operator;

    private final LoanApplicationEntity loanEntity = LoanApplicationEntity.builder()
            .idApplication(1L)
            .amount(BigDecimal.ONE)
            .term(1)
            .email("email@test.com")
            .status(1)
            .loanType(1)
            .build();


    private final LoanApplication loanApplication = LoanApplication.builder()
            .idApplication(1L)
            .amount(BigDecimal.ONE)
            .term(1)
            .email("email@test.com")
            .status(1)
            .loanType(1)
            .build();

    @Test
    void mustSaveValue() {

        when(mapper.map(loanEntity, LoanApplication.class)).thenReturn(loanApplication);
        when(mapper.map(loanApplication, LoanApplicationEntity.class)).thenReturn(loanEntity);
        when(repository.save(any(LoanApplicationEntity.class))).thenReturn(Mono.just(loanEntity));
        Mono<LoanApplication> result = repositoryAdapter.save(loanApplication);

        StepVerifier.create(result)
                .expectNext(loanApplication)
                .verifyComplete();
    }
}


