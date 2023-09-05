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
@ConfigurationProperties(prefix = "spring.security.oauth2")
@AllArgsConstructor
public class OAuth2Properties {
    @NotNull
    private final String cookieName;
    @NotNull
    private final String redirectUriSuccess;
    @NotNull
    private final String redirectUriFailure;
    @NotNull
    private final String redirectUriSignUp;
    private final int cookieExpireSeconds;
}
