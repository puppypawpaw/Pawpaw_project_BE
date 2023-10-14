package kr.co.pawpaw.api.config.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "custom.cookie")
@RequiredArgsConstructor
public class CookieProperties {
    @NotNull
    private final String domain;
    @NotNull
    private final String sameSite;
}
