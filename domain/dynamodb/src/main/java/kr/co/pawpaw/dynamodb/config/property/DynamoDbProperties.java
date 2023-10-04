package kr.co.pawpaw.dynamodb.config.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "amazon.aws")
public class DynamoDbProperties {
    private final String dynamoEndpoint;
    private final String signingRegion;
    private final String accessKey;
    private final String secretKey;
}