package kr.co.pawpaw.api.dto.auth;

import kr.co.pawpaw.api.dto.pet.CreatePetRequest;
import kr.co.pawpaw.api.dto.position.PositionRequest;
import kr.co.pawpaw.domainrdb.pet.domain.Pet;
import kr.co.pawpaw.domainrdb.pet.domain.PetType;
import kr.co.pawpaw.domainrdb.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class SignUpRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    private static final PositionRequest positionRequest = PositionRequest.builder()
        .latitude(36.8)
        .longitude(36.8)
        .name("36.8")
        .build();

    @Test
    @DisplayName("유효성 검증 테스트")
    void validationTest() {
        //given
        SignUpRequest request1 = SignUpRequest.builder().build();
        List<String> violationPropertyPaths1 = List.of("termAgrees", "nickname", "email", "password", "position", "petInfos");
        SignUpRequest request2 = SignUpRequest.builder()
            .termAgrees(List.of(1L, 2L, 3L))
            .email("email")
            .password("password")
            .nickname("nickname")
            .phoneNumber("phoneNumber")
            .petInfos(List.of())
            .position(positionRequest)
            .build();
        List<String> violationPropertyPaths2 = List.of("petInfos");
        SignUpRequest request3 = SignUpRequest.builder()
            .termAgrees(List.of(1L, 2L, 3L))
            .email("email")
            .password("password")
            .nickname("nickname")
            .phoneNumber("phoneNumber")
            .petInfos(List.of(
                CreatePetRequest.builder()
                    .petName("petName")
                    .petType(PetType.DOG)
                    .build()))
            .position(positionRequest)
            .build();
        List<String> violationPropertyPaths3 = List.of();
        SignUpRequest request4 = SignUpRequest.builder()
            .termAgrees(List.of(1L, 2L, 3L))
            .email("email")
            .password("password")
            .nickname("nickname")
            .phoneNumber("phoneNumber")
            .petInfos(List.of(
                CreatePetRequest.builder().build()
            ))
            .position(positionRequest)
            .build();
        List<String> violationPropertyPaths4 = List.of("petInfos[0].petType", "petInfos[0].petName");

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

    @Test
    @DisplayName("toUser 메소드 테스트")
    void toUser() {
        //given
        SignUpRequest request = SignUpRequest.builder()
            .termAgrees(List.of(1L, 2L, 3L))
            .email("email")
            .password("password")
            .nickname("nickname")
            .phoneNumber("phoneNumber")
            .petInfos(List.of(
                CreatePetRequest.builder()
                    .petName("petName")
                    .petType(PetType.CAT)
                    .build()))
            .position(positionRequest)
            .build();

        String password = UUID.randomUUID().toString();
        String name = "userName";

        //when
        User user = request.toUser(password, name);

        //then
        assertThat(user.getEmail()).isEqualTo(request.getEmail());
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getNickname()).isEqualTo(request.getNickname());
        assertThat(user.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
        assertThat(user.getPosition().getLongitude()).isEqualTo(request.getPosition().getLongitude());
        assertThat(user.getPosition().getLatitude()).isEqualTo(request.getPosition().getLatitude());
        assertThat(user.getPosition().getName()).isEqualTo(request.getPosition().getName());
        assertThat(user.getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("toPet 메소드 테스트")
    void toPet() {
        //given
        List<CreatePetRequest> petInfos = List.of(
            CreatePetRequest.builder()
                .petName("petName1")
                .petType(PetType.LIZARD)
                .build(),
            CreatePetRequest.builder()
                .petName("petName2")
                .petType(PetType.BIRD)
                .build());

        CreatePetRequest other = CreatePetRequest.builder()
            .petName("petName3")
            .petType(PetType.GUINEA_PIG)
            .build();

        SignUpRequest request = SignUpRequest.builder()
            .termAgrees(List.of(1L, 2L, 3L))
            .email("email")
            .password("password")
            .nickname("nickname")
            .phoneNumber("phoneNumber")
            .petInfos(petInfos)
            .position(positionRequest)
            .build();

        User user = User.builder().build();

        //when
        List<Pet> pets = request.toPet(user);

        //then
        assertThat(pets.size()).isEqualTo(2);
        assertThat(pets.get(0).getParent()).isEqualTo(user);
        assertThat(pets.get(1).getParent()).isEqualTo(user);
        assertThat(pets.stream().map(Pet::getName).collect(Collectors.toList()).contains(petInfos.get(0).getPetName())).isTrue();
        assertThat(pets.stream().map(Pet::getName).collect(Collectors.toList()).contains(petInfos.get(1).getPetName())).isTrue();
        assertThat(pets.stream().map(Pet::getName).collect(Collectors.toList()).contains(other.getPetName())).isFalse();
        assertThat(pets.stream().map(Pet::getPetType).collect(Collectors.toList()).contains(petInfos.get(0).getPetType())).isTrue();
        assertThat(pets.stream().map(Pet::getPetType).collect(Collectors.toList()).contains(petInfos.get(1).getPetType())).isTrue();
        assertThat(pets.stream().map(Pet::getPetType).collect(Collectors.toList()).contains(other.getPetType())).isFalse();
    }

    @Test
    @DisplayName("deletePhoneNumberHyphen 메소드 테스트")
    void deletePhoneNumberHyphen() {
        //given
        SignUpRequest request = SignUpRequest.builder()
            .termAgrees(List.of(1L, 2L, 3L))
            .email("email")
            .password("password")
            .nickname("nickname")
            .phoneNumber("010-1234-5678")
            .position(positionRequest)
            .build();

        //when
        request.deletePhoneNumberHyphen();

        //then
        assertThat(request.getPhoneNumber()).isEqualTo("01012345678");
    }
}