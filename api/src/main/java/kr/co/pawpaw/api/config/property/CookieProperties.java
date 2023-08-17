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
@ConfigurationProperties(prefix = "custom.cookie")
@AllArgsConstructor
public class CookieProperties {
    @NotNull
    private final String domain;
    @NotNull
    private final String sameSite;
}
