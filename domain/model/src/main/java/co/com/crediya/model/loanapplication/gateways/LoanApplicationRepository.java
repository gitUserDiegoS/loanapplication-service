package co.com.crediya.model.loanapplication.gateways;

import co.com.crediya.model.common.PageRequest;
import co.com.crediya.model.common.PageResponse;
import co.com.crediya.model.loanapplication.LoanApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface LoanApplicationRepository {

    Mono<LoanApplication> createLoanApplication(LoanApplication user);

    //Mono<PageResponse<PendingLoanApplication>> findByStatus(int status, PageRequest pageRequest);
    Flux<PendingLoanApplication> findByStatus(int status, PageRequest pageRequest);

    Mono<Long> countByStatus(int status);

}
