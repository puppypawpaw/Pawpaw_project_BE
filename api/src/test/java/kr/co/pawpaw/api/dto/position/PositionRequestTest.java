package kr.co.pawpaw.api.dto.position;

import kr.co.pawpaw.domainrdb.position.Position;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PositionRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void validationTest() {
        //given
        PositionRequest request1 = PositionRequest.builder()
            .build();
        PositionRequest request2 = PositionRequest.builder()
            .longitude(36.8)
            .build();
        PositionRequest request3 = PositionRequest.builder()
            .longitude(36.8)
            .latitude(36.7)
            .build();
        PositionRequest request4 = PositionRequest.builder()
            .longitude(36.8)
            .latitude(36.7)
            .name("name")
            .build();

        List<String> violationPropertyPaths1 = List.of("longitude", "latitude", "name");
        List<String> violationPropertyPaths2 = List.of("latitude", "name");
        List<String> violationPropertyPaths3 = List.of("name");
        List<String> violationPropertyPaths4 = List.of();

        //when
        Set<ConstraintViolation<PositionRequest>> violations1 = validator.validate(request1);
        Set<ConstraintViolation<PositionRequest>> violations2 = validator.validate(request2);
        Set<ConstraintViolation<PositionRequest>> violations3 = validator.validate(request3);
        Set<ConstraintViolation<PositionRequest>> violations4 = validator.validate(request4);

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
            assertThat(violations3).anyMatch(violation -> violation.getPropertyPath().toString().equals(violationPropertyPath));
        });
    }

    @Test
    void toEntity() {
        //given
        PositionRequest request = PositionRequest.builder()
            .longitude(36.8)
            .latitude(36.7)
            .name("name")
            .build();
        Position resultExpected = Position.builder()
            .longitude(request.getLongitude())
            .latitude(request.getLatitude())
            .name(request.getName())
            .build();
        //when
        Position result = request.toEntity();

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(resultExpected);
    }
}