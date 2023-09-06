package kr.co.pawpaw.api.dto.sms;

import kr.co.pawpaw.feignClient.dto.Recipient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class SendVerificationCodeRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("유효성 검증 테스트")
    void validationTest() {
        //given
        Recipient recipient = Recipient.builder()
            .to("to")
            .build();
        SendVerificationCodeRequest request1 = SendVerificationCodeRequest.builder()
            .build();
        SendVerificationCodeRequest request2 = SendVerificationCodeRequest.builder()
            .recipient(recipient)
            .build();
        SendVerificationCodeRequest request3 = SendVerificationCodeRequest.builder()
            .recipient(recipient)
            .birthday("birthday")
            .build();
        SendVerificationCodeRequest request4 = SendVerificationCodeRequest.builder()
            .recipient(recipient)
            .birthday("birthday")
            .name("name")
            .build();

        List<String> violationPropertyPaths1 = List.of("recipient", "birthday", "name");
        List<String> violationPropertyPaths2 = List.of("birthday", "name");
        List<String> violationPropertyPaths3 = List.of("name");
        List<String> violationPropertyPaths4 = List.of();

        //when
        Set<ConstraintViolation<SendVerificationCodeRequest>> violations1 = validator.validate(request1);
        Set<ConstraintViolation<SendVerificationCodeRequest>> violations2 = validator.validate(request2);
        Set<ConstraintViolation<SendVerificationCodeRequest>> violations3 = validator.validate(request3);
        Set<ConstraintViolation<SendVerificationCodeRequest>> violations4 = validator.validate(request4);

        //then
        assertThat(violations1).hasSize(violationPropertyPaths1.size());
        violationPropertyPaths1.forEach(violationPropertyPath -> {
            assertThat(violations1).anyMatch(violation -> violation.getPropertyPath().toString().equals(violationPropertyPath));
        });
        assertThat(violations2).hasSize(violationPropertyPaths2.size());
        violationPropertyPaths2.forEach(violationPropertyPath -> {
            assertThat(violations2).anyMatch(violation -> violation.getPropertyPath().toString().equals(violationPropertyPath));
        });
        assertThat(violations3).hasSize(violationPropertyPaths3.size());
        violationPropertyPaths3.forEach(violationPropertyPath -> {
            assertThat(violations3).anyMatch(violation -> violation.getPropertyPath().toString().equals(violationPropertyPath));
        });
        assertThat(violations4).hasSize(violationPropertyPaths4.size());
        violationPropertyPaths4.forEach(violationPropertyPath -> {
            assertThat(violations4).anyMatch(violation -> violation.getPropertyPath().toString().equals(violationPropertyPath));
        });
    }
}