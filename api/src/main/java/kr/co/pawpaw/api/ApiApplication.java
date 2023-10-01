package kr.co.pawpaw.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRedisRepositories(basePackages = "kr.co.pawpaw.redis.*")
@EnableJpaRepositories(basePackages = "kr.co.pawpaw.mysql.*")
@EntityScan(basePackages = "kr.co.pawpaw.mysql.*")
@EnableScheduling
@EnableJpaAuditing
@EnableFeignClients(basePackages = "kr.co.pawpaw.feignClient.*")
@ConfigurationPropertiesScan(basePackages = { "kr.co.pawpaw.api.*", "kr.co.pawpaw.redis.*" } )
@OpenAPIDefinition(
    servers = @Server(url = "/", description = "defaultServer")
)
@SpringBootApplication(scanBasePackages = {
    "kr.co.pawpaw.api",
    "kr.co.pawpaw.mysql.*",
    "kr.co.pawpaw.redis.*",
    "kr.co.pawpaw.feignClient.*",
    "kr.co.pawpaw.objectStorage.*",
    "kr.co.pawpaw.mail.*"
})
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
