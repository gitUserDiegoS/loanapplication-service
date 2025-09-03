package co.com.crediya.model.loanapplication.gateways;

import co.com.crediya.model.loanapplication.LoanType;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {

    Mono<LoanType> findByLoanType(Integer type);

}
