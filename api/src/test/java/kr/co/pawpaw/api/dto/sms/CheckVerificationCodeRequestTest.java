package kr.co.pawpaw.api.dto.sms;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CheckVerificationCodeRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("유효성 검증 테스트")
    void validationTest() {
        //given
        CheckVerificationCodeRequest request1 = CheckVerificationCodeRequest.builder()
            .build();
        CheckVerificationCodeRequest request2 = CheckVerificationCodeRequest.builder()
            .phoneNumber("phoneNumber")
            .build();
        CheckVerificationCodeRequest request3 = CheckVerificationCodeRequest.builder()
            .phoneNumber("phoneNumber")
            .code("code")
            .build();

        List<String> violationPropertyPaths1 = List.of("phoneNumber", "code");
        List<String> violationPropertyPaths2 = List.of("code");
        List<String> violationPropertyPaths3 = List.of();

        //when
        Set<ConstraintViolation<CheckVerificationCodeRequest>> violations1 = validator.validate(request1);
        Set<ConstraintViolation<CheckVerificationCodeRequest>> violations2 = validator.validate(request2);
        Set<ConstraintViolation<CheckVerificationCodeRequest>> violations3 = validator.validate(request3);

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
    }
}