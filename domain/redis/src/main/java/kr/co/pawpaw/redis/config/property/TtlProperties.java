package kr.co.pawpaw.redis.config.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "spring.redis.ttl")
@RequiredArgsConstructor
public class TtlProperties {
    private final long refreshToken;
    private final long verificationLifeTimeCode;
    private final long verificationLifeTimeSignUp;
    private final long verificationLifeTimeDefault;
    private final long oauth2TempAttributes;
    private final long changePasswordTempKey;
}
