package co.com.crediya.sqs.listener;

import co.com.crediya.model.loannotification.LoanNotification;
import co.com.crediya.model.loannotification.gateways.LoanNotificationRepository;
import co.com.crediya.model.loanresponseprocessor.LoanResponseProcessor;
import co.com.crediya.model.utilenum.StatusEnum;
import co.com.crediya.usecase.loanresponsequeue.LoanResponseQueueUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.time.Duration;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {

    private final ObjectMapper mapper;

    private final LoanResponseQueueUseCase loanResponseQueueUseCase;

    private final LoanNotificationRepository loannotificationRepository;


    @Override
    public Mono<Void> apply(Message message) {

        LoanResponseProcessor processUpdate = new LoanResponseProcessor();

        try {
            processUpdate = mapper.readValue(message.body(), LoanResponseProcessor.class);
        } catch (JsonProcessingException e) {
            return Mono.error(new RuntimeException("Error processing message", e));
        }

        return loanResponseQueueUseCase.processLoanResponse(processUpdate)
                .delayElement(Duration.ofSeconds(1))
                .then( // continue sending msg after update status
                        loannotificationRepository.send(
                                LoanNotification.builder()
                                        .idApplication(processUpdate.getIdApplication())
                                        .status(StatusEnum.translatefromText(processUpdate.getDecition()))
                                        .paymentPlan(processUpdate.getDecition().equalsIgnoreCase("APPROVED") ? processUpdate.getPaymentPlan() : null)
                                        .build()
                        )
                )
                .then();


    }


}
