package co.com.crediya.usecase.loanresponsequeue;

import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.model.loanapplication.gateways.LoanApplicationRepository;

import co.com.crediya.model.loanresponseprocessor.LoanResponseProcessor;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanResponseQueueUseCase implements IloanResponseQueueUseCase {


    private final LoanApplicationRepository loanApplicationRepository;


    @Override
    public Mono<Void> processLoanResponse(LoanResponseProcessor processUpdate) {


        int status = 1;
        if (processUpdate.getDecition().equalsIgnoreCase("REJECTED")) {
            status = 2;
        } else if (processUpdate.getDecition().equalsIgnoreCase("APPROVED")) {
            status = 3;

        }


        loanApplicationRepository.updateStatusLoanApplication(new LoanApplication(processUpdate.getIdApplication(), null, null, null, status, null))
                .doOnNext(updated -> System.out.println(" Solicitud {} actualizada en BD con estado={}" +
                        updated.getIdApplication() + "---" + updated.getStatus()))
                .doOnError(err -> System.out.println(" Error al actualizar la solicitud {}: {}" +
                        processUpdate.getIdApplication() + "---" + err.getMessage() + "---" + err))
                .doOnSuccess(done -> System.out.println("Flujo completado para solicitud {}" + processUpdate.getIdApplication()))
        ;
        return Mono.empty();
    }
}
