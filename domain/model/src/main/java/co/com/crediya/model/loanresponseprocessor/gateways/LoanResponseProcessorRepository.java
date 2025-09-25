package co.com.crediya.model.loanresponseprocessor.gateways;

import co.com.crediya.model.loanresponseprocessor.LoanResponseProcessor;
import reactor.core.publisher.Mono;

public interface LoanResponseProcessorRepository {

    Mono<String> processLoanResponse(LoanResponseProcessor response);

}
