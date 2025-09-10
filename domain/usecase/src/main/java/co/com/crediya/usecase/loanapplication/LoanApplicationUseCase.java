package co.com.crediya.usecase.loanapplication;

import co.com.crediya.model.common.PageRequest;
import co.com.crediya.model.common.PageResponse;
import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.model.loanapplication.User;
import co.com.crediya.model.loanapplication.constants.ExceptionMessages;
import co.com.crediya.model.loanapplication.exceptions.CreationNotAllowedException;

import co.com.crediya.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.crediya.model.loanapplication.gateways.LoanTypeRepository;
import co.com.crediya.model.loanapplication.gateways.PendingLoanApplication;
import co.com.crediya.model.loanapplication.gateways.UserGatewayRepository;
import co.com.crediya.model.loanapplication.exceptions.NotAllowedLoanTypeException;
import co.com.crediya.model.loanoperation.LoanOperation;
import co.com.crediya.model.usersession.UserSession;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class LoanApplicationUseCase implements IloanAppicationUseCase {

    private final UserGatewayRepository userGatewayRepository;

    private final LoanApplicationRepository loanApplicationRepository;

    private final LoanTypeRepository loanTypeRepository;


    @Override
    public Mono<LoanApplication> saveLoanApplication(LoanApplication loanApplication, String idDocument, String token, UserSession userSession) {

        return loanTypeRepository.findByLoanType(loanApplication.getLoanType())
                .switchIfEmpty(Mono.error(new NotAllowedLoanTypeException(String.format(ExceptionMessages.NOT_ALLOWED_LOAN_TYPE, loanApplication.getLoanType()))))
                .flatMap(validType -> userGatewayRepository.findUserByIdDocument(idDocument, token)
                        .flatMap(user -> {

                            if (!user.getEmail().equalsIgnoreCase(userSession.getEmail())) {
                                return Mono.error(new CreationNotAllowedException(ExceptionMessages.NOT_ALLOWED_USER));
                            }

                            loanApplication.setEmail(user.getEmail());
                            loanApplication.setStatus(1);

                            return loanApplicationRepository.createLoanApplication(loanApplication);
                        })

                );


    }


    @Override
    public Mono<PageResponse<PendingLoanApplication>> getLoanApplications(int status, PageRequest pageRequest, String token) {

        Mono<List<PendingLoanApplication>> loansMono =
                loanApplicationRepository.findByStatus(status, pageRequest)
                        .collectList();


        Mono<Long> countMono = loanApplicationRepository.countByStatus(status);

        return Mono.zip(loansMono, countMono)
                .flatMap(tuple -> {
                    List<PendingLoanApplication> loans = tuple.getT1();
                    Long total = tuple.getT2();


                    System.out.println("lista-->"+loans);

                    List<String> emails = loans.stream()
                            .map(PendingLoanApplication::getEmail)
                            .toList();

                    return userGatewayRepository.getUsersByEmailBatch(emails, token)
                            .collectMap(User::getEmail)
                            .map(usersMap -> {
                                List<PendingLoanApplication> enriched = loans.stream()
                                        .map(sol -> enrich(sol, usersMap.get(sol.getEmail())))
                                        .toList();

                                return new PageResponse<>(
                                        enriched,
                                        pageRequest.page(),
                                        pageRequest.size(),
                                        total
                                );
                            });
                });
/*
        return loanApplicationRepository.findByStatus(status, pageRequest)
                .flatMap(pendings -> {
                    List<String> emails = pendings.content().stream()
                            .map(PendingLoanApplication::getEmail)
                            .toList();

                    return userGatewayRepository.getUsersByEmailBatch(emails, token)
                            .collectMap(User::getEmail)
                            .map(usersMap -> {
                                List<PendingLoanApplication> enriched = pendings.content().stream()
                                        .map(sol -> enrich(sol, usersMap.get(sol.getEmail()))).toList();

                                return new PageResponse<>(
                                        enriched,
                                        pendings.page(),
                                        pendings.size(),
                                        pendings.total()
                                );
                            });
                });

 */
    }

    private PendingLoanApplication enrich(PendingLoanApplication sol, User user) {
        if (user == null) return sol;
        sol.setName(user.getName());
        sol.setBaseSalary(user.getSalaryBase());
        sol.setMonthlyFee(LoanOperation.calculateMonthlyFee(
                sol.getAmount(), sol.getTerm(), sol.getInterestRate()
        ));
        return sol;
    }

}
