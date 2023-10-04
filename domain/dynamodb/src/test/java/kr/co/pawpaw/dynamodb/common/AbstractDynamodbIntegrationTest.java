package kr.co.pawpaw.dynamodb.common;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest
public abstract class AbstractDynamodbIntegrationTest {
    private static final String DOCKER_IMAGE = "amazon/dynamodb-local";
    private static final String DOCKER_TAG = "latest";
    private static final int EXPOSED_PORT = 8000;

    public static GenericContainer dynamodb = new GenericContainer(String.format("%s:%s", DOCKER_IMAGE, DOCKER_TAG))
        .withReuse(true)
        .withExposedPorts(EXPOSED_PORT);

    static {
        dynamodb.start();
    }

    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        registry.add("amazon.aws.dynamo-endpoint", () -> String.format("http://%s:%s",
            dynamodb.getHost(),
            dynamodb.getMappedPort(EXPOSED_PORT)
        ));
        registry.add("amazon.aws.signing-region", () -> "ap-northeast-2");
        registry.add("amazon.aws.access-key", () -> "key");
        registry.add("amazon.aws.secret-key", () -> "key2");
        registry.add("spring.data.dynamodb.entity2ddl.auto", () -> "create");
        registry.add("spring.data.dynamodb.entity2ddl.gsiProjectionType", () -> "ALL");
        registry.add("spring.data.dynamodb.entity2ddl.readCapacity", () -> 10);
        registry.add("spring.data.dynamodb.entity2ddl.writeCapacity", () -> 1);
    }
}
