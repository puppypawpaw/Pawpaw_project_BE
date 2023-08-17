package kr.co.pawpaw.api.dto.term;

import kr.co.pawpaw.domainrdb.term.domain.Term;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CreateTermRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("유효성 검증 테스트")
    void validationTest() {
        //given
        CreateTermRequest request1 = CreateTermRequest.builder()
            .build();

        List<String> violationPropertyPaths1 = List.of("title", "content", "required");

        CreateTermRequest request2 = CreateTermRequest.builder()
            .title("title")
            .content("content")
            .order(1L)
            .required(true)
            .build();

        List<String> violationPropertyPaths2 = List.of();

        //when
        Set<ConstraintViolation<CreateTermRequest>> violations1 = validator.validate(request1);
        Set<ConstraintViolation<CreateTermRequest>> violations2 = validator.validate(request2);

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

    @Test
    @DisplayName("toEntity 테스트")
    void toEntity() {
        //given
        CreateTermRequest request = CreateTermRequest.builder()
            .title("title")
            .content("content")
            .order(1L)
            .required(true)
            .build();

        //when
        Term term = request.toEntity();

        //then
        assertThat(term.getTitle()).isEqualTo(request.getTitle());
        assertThat(term.getContent()).isEqualTo(request.getContent());
        assertThat(term.getOrder()).isEqualTo(request.getOrder());
        assertThat(term.getRequired()).isEqualTo(request.getRequired());
    }
}