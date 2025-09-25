package co.com.crediya.usecase.loanresponsequeue;

import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.model.loanapplication.gateways.LoanApplicationRepository;

import co.com.crediya.model.loannotification.gateways.LoanNotificationRepository;
import co.com.crediya.model.loanresponseprocessor.LoanResponseProcessor;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanResponseQueueUseCase implements IloanResponseQueueUseCase {


    private final LoanApplicationRepository loanApplicationRepository;

    private final LoanNotificationRepository loannotificationRepository;



    @Override
    public Mono<Void> processLoanResponse(LoanResponseProcessor processUpdate) {


        int status = 6;//status for manual review
        if (processUpdate.getDecition().equalsIgnoreCase("REJECTED")) {
            status = 3;
        } else if (processUpdate.getDecition().equalsIgnoreCase("APPROVED")) {
            status = 2;

        }


        return loanApplicationRepository.updateStatusLoanApplication(new LoanApplication(processUpdate.getIdApplication(), null, null, null, status, null))
                .then();

    }
}
