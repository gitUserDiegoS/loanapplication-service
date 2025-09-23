package co.com.crediya.model.loannotification.gateways;


import co.com.crediya.model.loanautomaticvalidation.LoanAutomaticValidation;
import co.com.crediya.model.loannotification.LoanNotification;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface LoanNotificationRepository {

    Mono<String> send(LoanNotification message);

    Mono<String> createAsyncJson(LoanNotification mail);

    //Gateways for automatic validation
    Mono<Void> sendForValidation(String payload);

    Flux<LoanAutomaticValidation> receiveResponses();

}
