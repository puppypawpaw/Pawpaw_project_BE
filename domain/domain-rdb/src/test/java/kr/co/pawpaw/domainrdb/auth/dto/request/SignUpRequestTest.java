package kr.co.pawpaw.domainrdb.auth.dto.request;

import helper.SignUpRequestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SignUpRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("유효성 검증 테스트")
    void validationTest() {
        //given
        SignUpRequest request1 = SignUpRequestBuilder.builder().build();
        List<String> violationPropertyPaths1 = List.of("termAgrees", "nickname", "id", "password", "passwordConfirm", "phoneNumber", "position", "petName", "petIntroduction");
        SignUpRequest request2 = SignUpRequestBuilder.builder()
            .termAgrees(List.of(1L, 2L, 3L))
            .id("id")
            .password("password")
            .passwordConfirm("password")
            .nickname("nickname")
            .phoneNumber("phoneNumber")
            .petName("")
            .petIntroduction("")
            .position("position")
            .build();
        List<String> violationPropertyPaths2 = List.of("petName", "petIntroduction");
        SignUpRequest request3 = SignUpRequestBuilder.builder()
            .termAgrees(List.of(1L, 2L, 3L))
            .id("id")
            .password("password")
            .passwordConfirm("password")
            .nickname("nickname")
            .phoneNumber("phoneNumber")
            .petName("petName")
            .petIntroduction("petIntroduction")
            .position("position")
            .build();
        List<String> violationPropertyPaths3 = List.of();
        SignUpRequest request4 = SignUpRequestBuilder.builder()
            .termAgrees(List.of(1L, 2L, 3L))
            .id("id")
            .password("password")
            .passwordConfirm("password")
            .nickname("nickname")
            .phoneNumber("phoneNumber")
            .petName("petNamepetNamepetName")
            .petIntroduction("petIntroductionpetIntroductionpetIntroductionpetIntroductionpetIntroduction")
            .position("position")
            .build();
        List<String> violationPropertyPaths4 = List.of("petName", "petIntroduction");

        //when
        Set<ConstraintViolation<SignUpRequest>> violations1 = validator.validate(request1);
        Set<ConstraintViolation<SignUpRequest>> violations2 = validator.validate(request2);
        Set<ConstraintViolation<SignUpRequest>> violations3 = validator.validate(request3);
        Set<ConstraintViolation<SignUpRequest>> violations4 = validator.validate(request4);

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