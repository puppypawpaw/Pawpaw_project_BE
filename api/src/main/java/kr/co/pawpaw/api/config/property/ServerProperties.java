package kr.co.pawpaw.api.config.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "server")
@AllArgsConstructor
public class ServerProperties {
    @NotNull
    private final List<String> allowedOrigins;
    private final int port;
}
