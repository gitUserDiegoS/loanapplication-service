package co.com.crediya.usecase.loanapplication;

import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.model.loanapplication.LoanType;
import co.com.crediya.model.loanapplication.User;
import co.com.crediya.model.loanapplication.exceptions.NotAllowedLoanTypeException;
import co.com.crediya.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.crediya.model.loanapplication.gateways.LoanTypeRepository;
import co.com.crediya.model.loanapplication.gateways.UserGatewayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanApplicationUserCaseTest {

    @Mock
    private UserGatewayRepository userGatewayRepository;

    @Mock
    private LoanApplicationRepository loanApplicationRepository;

    @Mock
    private LoanTypeRepository loanTypeRepository;

    @InjectMocks
    private LoanApplicationUseCase loanApplicationUseCase;

    @Test
    void saveLoanApplication_success() {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setLoanType(1);

        User user = new User();
        user.setIdDocument("123456");
        user.setEmail("test@email.com");

        LoanType loanType = new LoanType();
        loanType.setId(1);

        when(loanTypeRepository.findByLoanType(any(Integer.class)))
                .thenReturn(Mono.just(loanType));


        when(userGatewayRepository.findUserByIdDocument(anyString()))
                .thenReturn(Mono.just(user));

        when(loanApplicationRepository.createLoanApplication(any(LoanApplication.class)))
                .thenAnswer(invocation -> Mono.just(loanApplication));

        Mono<LoanApplication> result = loanApplicationUseCase.saveLoanApplication(loanApplication, "123456");

        StepVerifier.create(result)
                .expectNextMatches(app ->
                        app.getEmail().equals("test@email.com") &&
                                app.getStatus() == 1)
                .verifyComplete();

        verify(loanTypeRepository).findByLoanType(1);
        verify(userGatewayRepository).findUserByIdDocument("123456");
        verify(loanApplicationRepository).createLoanApplication(any(LoanApplication.class));

    }


    @Test
    void shouldFailWhenLoanTypeIsNotAllowed() {

        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setLoanType(15);

        when(loanTypeRepository.findByLoanType(any(Integer.class)))
                .thenReturn(Mono.empty());


        Mono<LoanApplication> result = loanApplicationUseCase.saveLoanApplication(loanApplication, "123456");

        // Assert
        StepVerifier.create(result)
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(NotAllowedLoanTypeException.class);
                })
                .verify();

        verify(loanTypeRepository).findByLoanType(15);
        verifyNoInteractions(userGatewayRepository);
        verifyNoInteractions(loanApplicationRepository);
    }


}
