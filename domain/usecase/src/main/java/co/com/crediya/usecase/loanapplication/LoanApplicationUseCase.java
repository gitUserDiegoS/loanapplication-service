package co.com.crediya.usecase.loanapplication;

import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.model.loanapplication.constants.ExceptionMessages;
import co.com.crediya.model.loanapplication.exceptions.CreationNotAllowedException;

import co.com.crediya.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.crediya.model.loanapplication.gateways.LoanTypeRepository;
import co.com.crediya.model.loanapplication.gateways.UserGatewayRepository;
import co.com.crediya.model.loanapplication.exceptions.NotAllowedLoanTypeException;
import co.com.crediya.model.usersession.UserSession;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Flux<LoanApplication> getLoanApplications(int status, int page, int size, String sort, String token) {
        return loanApplicationRepository.findByStatus(status, page, size, sort)
                .flatMap(loan ->
                        userGatewayRepository.findUserByEmail(loan.getEmail())

                        );
    }


}
