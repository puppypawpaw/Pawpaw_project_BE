package kr.co.pawpaw.api.dto.auth;

import kr.co.pawpaw.api.dto.pet.CreatePetRequest;
import kr.co.pawpaw.api.dto.position.PositionRequest;
import kr.co.pawpaw.domainrdb.pet.domain.Pet;
import kr.co.pawpaw.domainrdb.pet.domain.PetType;
import kr.co.pawpaw.domainrdb.user.domain.OAuth2Provider;
import kr.co.pawpaw.domainrdb.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class SocialSignUpRequestTest {
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
        SocialSignUpRequest request1 = SocialSignUpRequest.builder()
            .build();
        List<String> violationPropertyPaths1 = List.of("key", "termAgrees", "position", "petInfos", "noImage", "nickname");
        SocialSignUpRequest request2 = SocialSignUpRequest.builder()
            .key("key")
            .termAgrees(List.of(1L, 2L, 3L))
            .position(positionRequest)
            .noImage(false)
            .nickname("nick")
            .build();
        List<String> violationPropertyPaths2 = List.of("petInfos");
        SocialSignUpRequest request3 = SocialSignUpRequest.builder()
            .key("key")
            .termAgrees(List.of(1L, 2L, 3L))
            .position(positionRequest)
            .noImage(false)
            .nickname("nick")
            .petInfos(List.of(
                CreatePetRequest.builder().build()
            ))
            .build();
        List<String> violationPropertyPaths3 = List.of("petInfos[0].petName", "petInfos[0].petType");
        SocialSignUpRequest request4 = SocialSignUpRequest.builder()
            .key("key")
            .termAgrees(List.of(1L, 2L, 3L))
            .position(positionRequest)
            .noImage(false)
            .nickname("nick")
            .petInfos(List.of(
                CreatePetRequest.builder()
                    .petName("petName")
                    .petType(PetType.DOG)
                    .build()
            ))
            .build();
        List<String> violationPropertyPaths4 = List.of();

        //when
        Set<ConstraintViolation<SocialSignUpRequest>> violations1 = validator.validate(request1);
        Set<ConstraintViolation<SocialSignUpRequest>> violations2 = validator.validate(request2);
        Set<ConstraintViolation<SocialSignUpRequest>> violations3 = validator.validate(request3);
        Set<ConstraintViolation<SocialSignUpRequest>> violations4 = validator.validate(request4);

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
    @DisplayName("toUser 메서드 테스트")
    void toUser() {
        //given
        SocialSignUpRequest request = SocialSignUpRequest.builder()
            .termAgrees(List.of(1L, 2L, 3L))
            .noImage(false)
            .nickname("nick")
            .key("key")
            .petInfos(List.of(
                CreatePetRequest.builder()
                    .petName("petName")
                    .petType(PetType.CAT)
                    .build()))
            .position(positionRequest)
            .build();

        String email = "email";
        OAuth2Provider provider = OAuth2Provider.GOOGLE;

        //when
        User user = request.toUser(email, provider);

        //then
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getNickname()).isEqualTo(request.getNickname());
        assertThat(user.getPhoneNumber()).isNull();
        assertThat(user.getPosition().getLatitude()).isEqualTo(request.getPosition().getLatitude());
        assertThat(user.getPosition().getLongitude()).isEqualTo(request.getPosition().getLongitude());
        assertThat(user.getPosition().getName()).isEqualTo(request.getPosition().getName());
        assertThat(user.getProvider()).isEqualTo(provider);
        assertThat(user.getPassword()).isEqualTo("");
    }

    @Test
    @DisplayName("toPet 메서드 테스트")
    void toPet() {
        //given
        List<CreatePetRequest> petInfos = List.of(
            CreatePetRequest.builder()
                .petName("petName1")
                .petType(PetType.FISH)
                .build(),
            CreatePetRequest.builder()
                .petName("petName2")
                .petType(PetType.HAMSTER)
                .build());

        CreatePetRequest other = CreatePetRequest.builder()
            .petName("petName3")
            .petType(PetType.RABBIT)
            .build();

        SocialSignUpRequest request = SocialSignUpRequest.builder()
            .termAgrees(List.of(1L, 2L, 3L))
            .key("key")
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
}