package co.com.crediya.usecase.loanapplication;

import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.model.usersession.UserSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IloanAppicationUseCase {

    Mono<LoanApplication> saveLoanApplication(LoanApplication loanApplication, String idDocument, String token, UserSession userSession);

    Flux<LoanApplication> getLoanApplications(int status, int page, int size, String sort, String token);


}
