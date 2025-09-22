package co.com.crediya.usecase.loanapplication;

import co.com.crediya.model.common.PageResponse;
import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.model.loanapplication.LoanType;
import co.com.crediya.model.loanapplication.User;
import co.com.crediya.model.loanapplication.constants.ExceptionMessages;
import co.com.crediya.model.loanapplication.exceptions.CreationNotAllowedException;
import co.com.crediya.model.loanapplication.exceptions.NotAllowedLoanTypeException;
import co.com.crediya.model.loanapplication.exceptions.UserNotFoundException;
import co.com.crediya.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.crediya.model.loanapplication.gateways.LoanTypeRepository;
import co.com.crediya.model.loanapplication.gateways.PendingLoanApplication;
import co.com.crediya.model.loanapplication.gateways.UserGatewayRepository;

import co.com.crediya.model.loannotification.LoanNotification;
import co.com.crediya.model.loannotification.gateways.LoanNotificationRepository;
import co.com.crediya.model.usersession.UserSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanApplicationUserCaseTest {

    @Mock
    private UserGatewayRepository userGatewayRepository;

    @Mock
    private LoanApplicationRepository loanApplicationRepository;

    @Mock
    private LoanTypeRepository loanTypeRepository;

    @Mock
    private LoanNotificationRepository loannotificationRepository;

    @InjectMocks
    private LoanApplicationUseCase loanApplicationUseCase;

    private final User user = User.builder()
            .idDocument("123456")
            .email("test1@email.com")
            .build();

    private final User anotherUser = User.builder()
            .idDocument("123457")
            .email("test2@email.com")
            .build();

    private final User userFromNotSession = User.builder()
            .idDocument("12345678")
            .email("test2@email.com")
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
            .email("test1@email.com")
            .name("role")
            .build();

    private final PendingLoanApplication pendingLoanApplicationOne = PendingLoanApplication.builder()
            .amount(BigDecimal.ONE)
            .term(2)
            .email("test1@email.com")
            .name("name")
            .loanType("1")
            .interestRate(BigDecimal.ONE)
            .status("1")
            .baseSalary(BigDecimal.ONE)
            .monthlyFee(BigDecimal.ONE)
            .build();

    private final PendingLoanApplication pendingLoanApplicationTwo = PendingLoanApplication.builder()
            .amount(BigDecimal.TWO)
            .term(2)
            .email("test2@email.com")
            .name("name")
            .loanType("1")
            .interestRate(BigDecimal.TWO)
            .status("1")
            .baseSalary(BigDecimal.TWO)
            .monthlyFee(BigDecimal.TWO)
            .build();

    List<PendingLoanApplication> loansList = List.of(pendingLoanApplicationOne, pendingLoanApplicationTwo);

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
                .expectNext(loanApplication)
                .verifyComplete();

        verify(loanTypeRepository).findByLoanType(1);
        verify(userGatewayRepository).findUserByIdDocument("123456", "tokenJwt");
        verify(loanApplicationRepository).createLoanApplication(any(LoanApplication.class));

    }


    @Test
    void shouldFailWhenLoanTypeIsNotAllowed() {


        when(loanTypeRepository.findByLoanType(any(Integer.class)))
                .thenReturn(Mono.empty());


        Mono<LoanApplication> result = loanApplicationUseCase.saveLoanApplication(loanApplicationInvalidType, "123456", "tokenJwt", userSession);

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


        when(userGatewayRepository.findUserByIdDocument(anyString(), anyString()))
                .thenReturn(Mono.error(new UserNotFoundException(String.format(ExceptionMessages.USER_NOT_FOUND, "123456"))));

        Mono<LoanApplication> result = loanApplicationUseCase.saveLoanApplication(loanApplication, "123456", "tokenJwt", userSession);


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


        when(userGatewayRepository.findUserByIdDocument(anyString(), anyString()))
                .thenReturn(Mono.just(userFromNotSession));

        Mono<LoanApplication> result = loanApplicationUseCase.saveLoanApplication(loanApplication, "12345678", "tokenJwt", userSession);

        StepVerifier.create(result)
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(CreationNotAllowedException.class);
                    assertThat(ExceptionMessages.NOT_ALLOWED_USER).isEqualTo("Cannot create a loan application for another user");
                })
                .verify();

        verify(loanTypeRepository).findByLoanType(1);
        verifyNoInteractions(loanApplicationRepository);

    }


    @Test
    void shouldGetLoanApplicationsPaginated() {

        long mockCount = 2L;

        when(loanApplicationRepository.findByStatus(any(Integer.class), anyString(), any(Integer.class), any(Integer.class)))
                .thenReturn(Flux.fromIterable(loansList));
        when(loanApplicationRepository.countByStatus(any(Integer.class)))
                .thenReturn(Mono.just(mockCount));

        when(userGatewayRepository.getUsersByEmailBatch(any(Flux.class), any(String.class)))
                .thenReturn(Flux.just(user, anotherUser));


        Mono<PageResponse<PendingLoanApplication>> resultMono =
                loanApplicationUseCase.getLoanApplications(1, "test2@email.com", 0, 5, 0, "token");

        resultMono.blockOptional().ifPresent(response -> {
            assertNotNull(response);
            assertEquals(mockCount, response.totalElements());
            assertEquals(5, response.size());
            assertEquals(0, response.page());
            assertEquals(2, response.content().size());

            PendingLoanApplication enrichedLoan1 = response.content().get(0);
            assertEquals("test1@email.com", enrichedLoan1.getEmail());

            PendingLoanApplication enrichedLoan2 = response.content().get(1);
            assertEquals("test2@email.com", enrichedLoan2.getEmail());
        });


    }

    @Test
    void updateLoanApplication_should_update_status_and_send_notification() {

        loanApplication.setStatus(2);


        when(loanApplicationRepository.updateStatusLoanApplication(any(LoanApplication.class)))
                .thenReturn(Mono.just(loanApplication));


        when(loannotificationRepository.send(any(LoanNotification.class)))
                .thenReturn(Mono.empty());

        Mono<LoanApplication> resultMono = loanApplicationUseCase.updateLoanApplication(loanApplication, "tokem");

        StepVerifier.create(resultMono)
                .expectNext(loanApplication)
                .verifyComplete();


    }


}
