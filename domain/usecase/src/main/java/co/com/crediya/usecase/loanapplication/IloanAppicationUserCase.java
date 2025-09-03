package co.com.crediya.usecase.loanapplication;

import co.com.crediya.model.loanapplication.LoanApplication;
import reactor.core.publisher.Mono;

public interface IloanAppicationUserCase {

    Mono<LoanApplication> saveLoanApplication(LoanApplication loanApplication, String idDocument);

}
