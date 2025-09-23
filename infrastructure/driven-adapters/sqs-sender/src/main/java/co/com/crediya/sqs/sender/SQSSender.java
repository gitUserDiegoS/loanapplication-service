package co.com.crediya.sqs.sender;

import co.com.crediya.model.loanautomaticvalidation.LoanAutomaticValidation;
import co.com.crediya.model.loannotification.LoanNotification;
import co.com.crediya.model.loannotification.gateways.LoanNotificationRepository;
import co.com.crediya.sqs.sender.config.SQSSenderProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.List;

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



    //Automatic Validation
    @Override
    public Mono<Void> sendForValidation(String payload) {
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(properties.queueDebCapacityRequestUrl())
                .messageBody(payload)
                .build();

        return Mono.fromFuture(() -> client.sendMessage(request))
                .then();
    }

    @Override
    public Flux<LoanAutomaticValidation> receiveResponses() {
        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(properties.queueDebCapacityResponseUrl())
                .waitTimeSeconds(10)
                .maxNumberOfMessages(5)
                .build();

        return Mono.fromFuture(() -> client.receiveMessage(request))
                .flatMapMany(response -> Flux.fromIterable(response.messages()))
                .map(this::toModel);
    }

    private LoanAutomaticValidation toModel(Message msg) {
        return LoanAutomaticValidation.builder()
                .id(msg.messageId())
                .body(msg.body())
                .build();
    }


}
