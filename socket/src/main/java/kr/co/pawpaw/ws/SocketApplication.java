package kr.co.pawpaw.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@ConfigurationPropertiesScan(basePackages = {
    "kr.co.pawpaw.mysql",
    "kr.co.pawpaw.dynamodb",
    "kr.co.pawpaw.ws",
    "kr.co.pawpaw.redis"
})
@EnableJpaRepositories(basePackages = "kr.co.pawpaw.mysql")
@EntityScan(basePackages = "kr.co.pawpaw.mysql")
@EnableRedisRepositories(basePackages = "kr.co.pawpaw.redis")
@SpringBootApplication(scanBasePackages = {
    "kr.co.pawpaw.mysql",
    "kr.co.pawpaw.dynamodb",
    "kr.co.pawpaw.ws",
    "kr.co.pawpaw.redis"
})
public class SocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(SocketApplication.class, args);
    }

}
