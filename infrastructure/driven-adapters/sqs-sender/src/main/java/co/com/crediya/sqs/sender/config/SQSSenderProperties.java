package co.com.crediya.sqs.sender.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapter.sqs")
public record SQSSenderProperties(
        String region,
        String queueUrl,
        String queueDebCapacityRequestUrl,
        String queueDebCapacityResponseUrl,
        String endpoint,
        Credentials credentials) {

    public record Credentials(
            String accessKey,
            String secretKey

    ) {
    }


}


