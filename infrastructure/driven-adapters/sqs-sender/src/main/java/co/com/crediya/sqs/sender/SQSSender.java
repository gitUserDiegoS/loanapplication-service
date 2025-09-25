package co.com.crediya.sqs.sender;

import co.com.crediya.model.loanautomaticvalidation.LoanAutomaticValidation;
import co.com.crediya.model.loannotification.LoanNotification;
import co.com.crediya.model.loannotification.LoanNotificationRequest;
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

    private SendMessageRequest buildRequestAutomaticValidation(String message) {
        return SendMessageRequest.builder()
                .queueUrl(properties.queueDebCapacityRequestUrl())
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

    public <T> Mono<String> createAsyncJson(T sqsRequest) {
        return Mono.fromCallable(() -> mapper.writeValueAsString(sqsRequest));
    }


    //Automatic Validation
    @Override
    public Mono<String> sendForValidation(LoanNotificationRequest payload) {
        return createAsyncJson(payload)
                .map(this::buildRequestAutomaticValidation)
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .doOnError(response -> log.error("error when send {}", response.getMessage()))
                .map(SendMessageResponse::messageId);

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
