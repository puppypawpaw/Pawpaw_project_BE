package kr.co.pawpaw.dynamodb.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import kr.co.pawpaw.dynamodb.config.property.DynamoDbProperties;
import lombok.RequiredArgsConstructor;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.util.ObjectUtils;

@Configuration
@RequiredArgsConstructor
@EnableDynamoDBRepositories(basePackages = {
    "kr.co.pawpaw.dynamodb"
}, includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = EnableScan.class))
public class DynamoDBConfig {
    private final DynamoDbProperties dynamoDbProperties;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AmazonDynamoDBClientBuilder amazonDynamoDBClientBuilder = AmazonDynamoDBClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(amazonAWSCredentials()));

        if (!ObjectUtils.isEmpty(dynamoDbProperties.getDynamoEndpoint())) {
            amazonDynamoDBClientBuilder.setEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(dynamoDbProperties.getDynamoEndpoint(), dynamoDbProperties.getSigningRegion()));
        }

        return amazonDynamoDBClientBuilder.build();
    }

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(
            dynamoDbProperties.getAccessKey(),
            dynamoDbProperties.getSecretKey()
        );
    }
}
