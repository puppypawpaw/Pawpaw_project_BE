package kr.co.pawpaw.domainredis.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "custom.verification.lifetime")
@RequiredArgsConstructor
public class VerificationLifeTimeProperties {
    private final Long code;
    private final Long signup;
    private final Long defaultTtl;
}