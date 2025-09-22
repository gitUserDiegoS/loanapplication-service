package co.com.crediya.model.loannotification.gateways;


import co.com.crediya.model.loannotification.LoanNotification;
import reactor.core.publisher.Mono;

public interface LoanNotificationRepository {

    Mono<String> send(LoanNotification message);

    Mono<String> createAsyncJson(LoanNotification mail);
}
