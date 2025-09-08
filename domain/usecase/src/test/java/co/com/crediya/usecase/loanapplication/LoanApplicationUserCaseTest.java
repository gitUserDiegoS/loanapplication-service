package co.com.crediya.usecase.loanapplication;

import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.model.loanapplication.LoanType;
import co.com.crediya.model.loanapplication.User;
import co.com.crediya.model.loanapplication.exceptions.NotAllowedLoanTypeException;
import co.com.crediya.model.loanapplication.exceptions.UserNotFoundException;
import co.com.crediya.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.crediya.model.loanapplication.gateways.LoanTypeRepository;
import co.com.crediya.model.loanapplication.gateways.UserGatewayRepository;

import co.com.crediya.model.usersession.UserSession;
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

    private final User user = User.builder()
            .idDocument("123456")
            .email("test@email.com")
            .build();

    private final LoanApplication loanApplication = LoanApplication.builder()
            .loanType(1)
            .build();

    private final LoanApplication loanApplicationInvalidType = LoanApplication.builder()
            .loanType(15)
            .build();

    private final LoanType loanType = LoanType.builder()
            .id(1)
            .build();

    private final UserSession userSession = UserSession.builder()
            .email("test@email.com")
            .name("role")
            .build();

    @Test
    void saveLoanApplication_success() {


        when(loanTypeRepository.findByLoanType(any(Integer.class)))
                .thenReturn(Mono.just(loanType));


        when(userGatewayRepository.findUserByIdDocument(anyString(), anyString()))
                .thenReturn(Mono.just(user));

        when(loanApplicationRepository.createLoanApplication(any(LoanApplication.class)))
                .thenAnswer(invocation -> Mono.just(loanApplication));

        Mono<LoanApplication> result = loanApplicationUseCase.saveLoanApplication(loanApplication, "123456", "tokenJwt", userSession);

        StepVerifier.create(result)
                .expectNextMatches(app ->
                        app.getEmail().equals("test@email.com") &&
                                app.getStatus() == 1)
                .verifyComplete();

        verify(loanTypeRepository).findByLoanType(1);
        verify(userGatewayRepository).findUserByIdDocument("123456","tokenJwt");
        verify(loanApplicationRepository).createLoanApplication(any(LoanApplication.class));

    }


    @Test
    void shouldFailWhenLoanTypeIsNotAllowed() {


        when(loanTypeRepository.findByLoanType(any(Integer.class)))
                .thenReturn(Mono.empty());


        Mono<LoanApplication> result = loanApplicationUseCase.saveLoanApplication(loanApplicationInvalidType, "123456","tokenJwt", userSession);

        StepVerifier.create(result)
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(NotAllowedLoanTypeException.class);
                })
                .verify();

        verify(loanTypeRepository).findByLoanType(15);
        verifyNoInteractions(userGatewayRepository);
        verifyNoInteractions(loanApplicationRepository);
    }

    @Test
    void shouldFailWhenUserNotFound() {


        when(loanTypeRepository.findByLoanType(any(Integer.class)))
                .thenReturn(Mono.just(loanType));


        when(userGatewayRepository.findUserByIdDocument(anyString(),anyString()))
                .thenReturn(Mono.error(new UserNotFoundException("123456")));

        Mono<LoanApplication> result = loanApplicationUseCase.saveLoanApplication(loanApplication, "123456","tokenJwt", userSession);


        StepVerifier.create(result)
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(UserNotFoundException.class);
                })
                .verify();

        verify(loanTypeRepository).findByLoanType(1);
        verifyNoInteractions(loanApplicationRepository);

    }

    @Test
    void shouldFailWhenCreateLoanApplicationForAnotherUsers() {


        when(loanTypeRepository.findByLoanType(any(Integer.class)))
                .thenReturn(Mono.just(loanType));


        when(userGatewayRepository.findUserByIdDocument(anyString(),anyString()))
                .thenReturn(Mono.error(new UserNotFoundException("123456")));

        Mono<LoanApplication> result = loanApplicationUseCase.saveLoanApplication(loanApplication, "123456","tokenJwt", userSession);


        StepVerifier.create(result)
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(UserNotFoundException.class);
                })
                .verify();

        verify(loanTypeRepository).findByLoanType(1);
        verifyNoInteractions(loanApplicationRepository);

    }

}
