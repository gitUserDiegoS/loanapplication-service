package co.com.crediya.sqs.sender;

import co.com.crediya.model.loannotification.LoanNotification;
import co.com.crediya.model.loannotification.gateways.LoanNotificationRepository;
import co.com.crediya.sqs.sender.config.SQSSenderProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements LoanNotificationRepository {

    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private final ObjectMapper mapper = new ObjectMapper();


    private SendMessageRequest buildRequest(String message) {
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(message)
                .build();
    }

    @Override
    public Mono<String> send(LoanNotification message) {
        return createAsyncJson(message)
                .map(this::buildRequest)
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .doOnError(response -> log.error("error when send {}", response.getMessage()))
                .map(SendMessageResponse::messageId);
    }

    @Override
    public Mono<String> createAsyncJson(LoanNotification notification) {
        return Mono.fromCallable(() -> mapper.writeValueAsString(notification));
    }
}
