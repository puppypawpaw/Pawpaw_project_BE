package kr.co.pawpaw.api.dto.pet;

import kr.co.pawpaw.domainrdb.pet.domain.PetType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CreatePetRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("유효성 검증 테스트")
    void validationTest() {
        //given
        CreatePetRequest test1 = CreatePetRequest.builder()
            .build();
        List<String> violationPropertyPaths1 = List.of("petName", "petType");
        CreatePetRequest test2 = CreatePetRequest.builder()
            .petName("petName")
            .petType(PetType.DOG)
            .build();
        List<String> violationPropertyPaths2 = List.of();
        CreatePetRequest test3 = CreatePetRequest.builder()
            .petName("petNamepetNamepetNamepetNamepetNamepetName")
            .build();
        List<String> violationPropertyPaths3 = List.of("petName", "petType");

        //when
        Set<ConstraintViolation<CreatePetRequest>> violations1 = validator.validate(test1);
        Set<ConstraintViolation<CreatePetRequest>> violations2 = validator.validate(test2);
        Set<ConstraintViolation<CreatePetRequest>> violations3 = validator.validate(test3);

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