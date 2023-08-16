package kr.co.pawpaw.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
@EnableRedisRepositories(basePackages = {"kr.co.pawpaw.*"})
@SpringBootApplication(scanBasePackages = "kr.co.pawpaw.*")
public class SocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocketApplication.class, args);
    }

}
