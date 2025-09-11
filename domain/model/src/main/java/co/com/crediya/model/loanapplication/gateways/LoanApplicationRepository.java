package co.com.crediya.model.loanapplication.gateways;


import co.com.crediya.model.loanapplication.LoanApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface LoanApplicationRepository {

    Mono<LoanApplication> createLoanApplication(LoanApplication user);

    Flux<PendingLoanApplication> findByStatus(int status, String email, int size, int offset);

    Mono<Long> countByStatus(int status);

}
