package co.com.crediya.usecase.loanresponsequeue;

import co.com.crediya.model.loanresponseprocessor.LoanResponseProcessor;
import reactor.core.publisher.Mono;

public interface IloanResponseQueueUseCase {

    Mono<Void> processLoanResponse(LoanResponseProcessor response);
}
