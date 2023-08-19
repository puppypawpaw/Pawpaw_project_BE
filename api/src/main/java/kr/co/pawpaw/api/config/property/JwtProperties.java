package kr.co.pawpaw.api.config.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "custom.jwt")
@AllArgsConstructor
public class JwtProperties {
    @NotNull
    private final Long accessTokenLifeTime;
    @NotNull
    private final Long refreshTokenLifeTime;
}
