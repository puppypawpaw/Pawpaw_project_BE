package kr.co.pawpaw.api.config.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "custom.sms.send-limit")
@AllArgsConstructor
public class SmsSendLimitProperties {
    private final int signUp;
}
