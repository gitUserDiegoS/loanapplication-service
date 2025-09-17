package co.com.crediya.model.loannotification.gateways;

import co.com.crediya.model.loanapplication.LoanApplication;
import reactor.core.publisher.Mono;

public interface LoannotificationRepository {

    Mono<Void> sendLoanStatusNotification(LoanApplication loan);
}
