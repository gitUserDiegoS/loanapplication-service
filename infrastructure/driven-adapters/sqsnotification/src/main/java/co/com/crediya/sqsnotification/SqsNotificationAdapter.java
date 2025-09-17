package co.com.crediya.sqsnotification;

import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.model.loannotification.gateways.LoannotificationRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
public class SqsNotificationAdapter implements LoannotificationRepository {


    private final SqsAsyncClient sqsAsyncClient;
    private final String queueUrl = "https://sqs.sa-east-1.amazonaws.com/027419662433/loan-notifier-sqs";

    public SqsNotificationAdapter(SqsAsyncClient sqsAsyncClient) {
        this.sqsAsyncClient = sqsAsyncClient;
    }

    @Override
    public Mono<Void> sendLoanStatusNotification(LoanApplication loan) {
        loan.setStatus(3);
        loan.setIdApplication(888888L);

        String payload = "{ \"idApplication\": \"" + loan.getIdApplication() +
                "\", \"status\": \"" + loan.getStatus() + "\" }";

        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(payload)
                .build();

        return Mono.fromFuture(() -> sqsAsyncClient.sendMessage(request)).then();
    }
}
