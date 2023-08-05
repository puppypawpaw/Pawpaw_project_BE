package com.puppy.pawpaw_project_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PawpawProjectBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PawpawProjectBeApplication.class, args);
    }

}
