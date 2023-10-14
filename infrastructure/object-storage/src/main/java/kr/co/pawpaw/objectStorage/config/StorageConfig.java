package kr.co.pawpaw.objectStorage.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class StorageConfig {
    private final String endPoint;
    private final String accessKey;
    private final String secretKey;
    private final String bucket;
    private final String publicUrl;

    public StorageConfig(
        @Value("${spring.storage.end-point}") final String endPoint,
        @Value("${spring.storage.access-key}") final String accessKey,
        @Value("${spring.storage.secret-key}") final String secretKey,
        @Value("${spring.storage.bucket}") final String bucket,
        @Value("${spring.storage.public-url}") final String publicUrl
    ) {
        this.endPoint = endPoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucket = bucket;
        this.publicUrl = publicUrl;
    }

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder
            .standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, null))
            .withPathStyleAccessEnabled(true)
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                accessKey,
                secretKey
            )))
            .build();
    }
}