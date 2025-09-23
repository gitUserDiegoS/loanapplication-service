package co.com.crediya.model.loanautomaticvalidation.gateways;

import reactor.core.publisher.Mono;

public interface LoanAutomaticValidationRepository {


    //Gateways for automatic validation
    Mono<Void> sendForValidation(String payload);

}
