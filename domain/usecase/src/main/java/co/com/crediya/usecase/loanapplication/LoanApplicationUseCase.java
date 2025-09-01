package co.com.crediya.usecase.loanapplication;

import co.com.crediya.model.loanapplication.LoanApplication;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanApplicationUseCase implements IloanAppicationUserCase {
    @Override
    public Mono<LoanApplication> saveUser(LoanApplication user) {
        return null;
    }
}
