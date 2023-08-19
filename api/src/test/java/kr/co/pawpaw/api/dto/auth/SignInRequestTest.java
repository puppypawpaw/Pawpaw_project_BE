package kr.co.pawpaw.api.dto.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SignInRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("유효성 검증 테스트")
    void validationTest() {
        //given
        SignInRequest request1 = SignInRequest.builder().build();
        List<String> violationPropertyPaths1 = List.of("email", "password");
        SignInRequest request2 = SignInRequest.builder()
            .email("email")
            .password("password")
            .build();
        List<String> violationPropertyPaths2 = List.of();

        //when
        Set<ConstraintViolation<SignInRequest>> violations1 = validator.validate(request1);
        Set<ConstraintViolation<SignInRequest>> violations2 = validator.validate(request2);

        //then
        assertThat(violations1).hasSize(violationPropertyPaths1.size());
        violationPropertyPaths1.forEach(violationPropertyPath -> {
            assertThat(violations1).anyMatch(violation -> violation.getPropertyPath().toString().equals(violationPropertyPath));
        });
        assertThat(violations2).hasSize(violationPropertyPaths2.size());
        violationPropertyPaths2.forEach(violationPropertyPath -> {
            assertThat(violations2).anyMatch(violation -> violation.getPropertyPath().toString().equals(violationPropertyPath));
        });
    }
}