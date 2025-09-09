package co.com.crediya.model.loanapplication.gateways;

import co.com.crediya.model.loanapplication.LoanApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanApplicationRepository {

    Mono<LoanApplication> createLoanApplication(LoanApplication user);

    Flux<LoanApplication> findByStatus(int status, int page, int size, String sort);

    //Mono<Long> countByStatus(int status);

}
