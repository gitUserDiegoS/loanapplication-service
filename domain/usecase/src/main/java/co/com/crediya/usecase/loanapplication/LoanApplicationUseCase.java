package co.com.crediya.usecase.loanapplication;

import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.crediya.model.loanapplication.gateways.LoanTypeRepository;
import co.com.crediya.model.loanapplication.gateways.UserGatewayRepository;
import co.com.crediya.model.loanapplication.exceptions.NotAllowedLoanTypeException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanApplicationUseCase implements IloanAppicationUserCase {

    private final UserGatewayRepository userGatewayRepository;

    private final LoanApplicationRepository loanApplicationRepository;

    private final LoanTypeRepository loanTypeRepository;


    @Override
    public Mono<LoanApplication> saveLoanApplication(LoanApplication loanApplication, String IdDocument) {

        return loanTypeRepository.findByLoanType(loanApplication.getLoanType())
                .switchIfEmpty(Mono.error(new NotAllowedLoanTypeException(loanApplication.getLoanType().toString())))
                .flatMap(validType -> userGatewayRepository.findUserByIdDocument(IdDocument)
                        .map(user -> {
                            loanApplication.setEmail(user.getEmail());
                            loanApplication.setStatus(1);
                            return loanApplication;
                        }))
                .flatMap(loanApplicationRepository::createLoanApplication);
    }

}
