package co.com.crediya.model.loannotification.gateways;


import co.com.crediya.model.loanautomaticvalidation.LoanAutomaticValidation;
import co.com.crediya.model.loannotification.LoanNotification;
import co.com.crediya.model.loannotification.LoanNotificationRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface LoanNotificationRepository {

    Mono<String> send(LoanNotification message);

    //Gateways for automatic validation
    Mono<String> sendForValidation(LoanNotificationRequest payload);

    Flux<LoanAutomaticValidation> receiveResponses();

}
