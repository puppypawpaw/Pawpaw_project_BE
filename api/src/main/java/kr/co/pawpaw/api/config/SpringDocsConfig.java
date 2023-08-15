package kr.co.pawpaw.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;

@Configuration
public class SpringDocsConfig {
    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
            .title("PawPaw API 문서")
            .version("v1")
            .description("잘못된 부분이나 오류 발생 시 바로 말씀해주세요.")
            .contact(new Contact()
                .email("tnt7986@gmail.com"));


        SecurityRequirement addSecurityItem = new SecurityRequirement();
        addSecurityItem.addList("JWT");

        return new OpenAPI()
            .addSecurityItem(addSecurityItem)
            .info(info);
    }

}
