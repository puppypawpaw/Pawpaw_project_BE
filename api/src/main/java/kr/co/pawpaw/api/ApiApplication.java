package kr.co.pawpaw.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRedisRepositories(basePackages = "kr.co.pawpaw.*")
@EnableJpaRepositories(basePackages = "kr.co.pawpaw.*")
@EntityScan(basePackages = "kr.co.pawpaw.*")
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = "kr.co.pawpaw.*")
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
