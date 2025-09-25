package co.com.crediya.sqs.listener.helper;

import co.com.crediya.model.loanautomaticvalidation.LoanAutomaticValidation;
import co.com.crediya.model.loannotification.LoanNotification;
import co.com.crediya.model.loannotification.LoanNotificationRequest;
import co.com.crediya.model.loannotification.gateways.LoanNotificationRepository;
import co.com.crediya.sqs.listener.SQSProcessor;
import co.com.crediya.sqs.listener.config.SQSProperties;
import co.com.crediya.usecase.loanresponsequeue.LoanResponseQueueUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageResponse;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SQSListenerTest {

    @Mock
    private SqsAsyncClient asyncClient;

    @Mock
    private SQSProperties sqsProperties;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        var sqsProperties = new SQSProperties(
                "us-east-1",
                "http://localhost:4566",
                "http://localhost:4566/00000000000/queueName",
                20,
                30,
                10,
                1,
                null
        );

        var message = Message.builder().body("message").build();
        var deleteMessageResponse = DeleteMessageResponse.builder().build();
        var messageResponse = ReceiveMessageResponse.builder().messages(message).build();

        when(asyncClient.receiveMessage(any(ReceiveMessageRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(messageResponse));
        when(asyncClient.deleteMessage(any(DeleteMessageRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(deleteMessageResponse));
    }

    @Test
    void listenerTest() {
        var sqsListener = SQSListener.builder()
                .client(asyncClient)
                .properties(sqsProperties)
                .processor(new SQSProcessor(new ObjectMapper(), new LoanResponseQueueUseCase(null,null), new LoanNotificationRepository() {
                    @Override
                    public Mono<String> send(LoanNotification message) {
                        return null;
                    }

                    @Override
                    public Mono<String> sendForValidation(LoanNotificationRequest payload) {
                        return null;
                    }

                    @Override
                    public Flux<LoanAutomaticValidation> receiveResponses() {
                        return null;
                    }
                }))
                .operation("operation")
                .build();

        Flux<Void> flow = ReflectionTestUtils.invokeMethod(sqsListener, "listen");
        StepVerifier.create(flow).verifyComplete();
    }
}
