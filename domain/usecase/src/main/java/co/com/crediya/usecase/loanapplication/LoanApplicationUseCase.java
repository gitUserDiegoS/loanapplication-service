package co.com.crediya.usecase.loanapplication;

import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.model.loanapplication.User;
import co.com.crediya.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.crediya.model.loanapplication.gateways.UserGatewayRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanApplicationUseCase implements IloanAppicationUserCase {

    private final UserGatewayRepository userGatewayRepository;

    private final LoanApplicationRepository loanApplicationRepository;

    @Override
    public Mono<LoanApplication> saveLoanApplication(LoanApplication loanApplication, String IdDocument) {


        return userGatewayRepository.findUserByIdDocument(IdDocument)
                .flatMap(user -> {
                    loanApplication.setEmail(user.getEmail());
                    loanApplication.setStatus(1);
                    return loanApplicationRepository.createLoanApplication(loanApplication);
                });
    }


}
