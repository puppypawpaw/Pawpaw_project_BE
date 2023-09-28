package kr.co.pawpaw.api.service.pet;

import kr.co.pawpaw.api.dto.pet.CreatePetRequest;
import kr.co.pawpaw.api.dto.pet.CreatePetResponse;
import kr.co.pawpaw.api.dto.pet.PetResponse;
import kr.co.pawpaw.common.exception.pet.NotFoundPetException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.pet.domain.Pet;
import kr.co.pawpaw.domainrdb.pet.domain.PetType;
import kr.co.pawpaw.domainrdb.pet.service.command.PetCommand;
import kr.co.pawpaw.domainrdb.pet.service.query.PetQuery;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("PetService의")
@ExtendWith(MockitoExtension.class)
class PetServiceTest {
    @Mock
    private UserQuery userQuery;
    @Mock
    private PetQuery petQuery;
    @Mock
    private PetCommand petCommand;

    @InjectMocks
    private PetService petService;

    @Nested
    @DisplayName("createPet 메서드는")
    class CreatePet {
        private final User user = User.builder().build();
        private final CreatePetRequest createPetRequest = CreatePetRequest.builder()
            .petType(PetType.DOG)
            .petName("루이")
            .build();
        private final Pet pet = createPetRequest.toEntity(user);

        @Test
        @DisplayName("존재하지 않는 유저 아이디를 입력받으면 예외가 발생한다.")
        void NotFoundUserException() {
            //given
            when(userQuery.findByUserId(user.getUserId()))
                .thenReturn(Optional.empty());

            //when
            assertThatThrownBy(() -> petService.createPet(user.getUserId(), createPetRequest))
                .isInstanceOf(NotFoundUserException.class);

            //then
        }

        @Test
        @DisplayName("생성된 Pet entity로 만든 CreatePetResponse를 반환한다.")
        void returnCreatePetResponse() throws NoSuchFieldException, IllegalAccessException {
            //given
            Field idField = pet.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(pet, 123L);

            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            when(petCommand.save(any(Pet.class))).thenReturn(pet);

            CreatePetResponse expectedResult = CreatePetResponse.of(pet);

            //when
            CreatePetResponse result = petService.createPet(user.getUserId(), createPetRequest);

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
        }
    }

    @Nested
    @DisplayName("getPetList 메서드는")
    class getPetList {
        User user = User.builder().build();
        List<Pet> petList = List.of(
            Pet.builder()
                .petType(PetType.DOG)
                .name("강아지 이름")
                .introduction("강아지 소개")
                .parent(user)
                .build(),
            Pet.builder()
                .petType(PetType.CAT)
                .name("고양이 이름")
                .introduction("고양이 소개")
                .parent(user)
                .build()
        );

        @Test
        @DisplayName("존재하지 않는 유저 아이디를 입력받으면 예외가 발생한다.")
        void NotFoundUserException() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.empty());

            //when
            assertThatThrownBy(() -> petService.getPetList(user.getUserId())).isInstanceOf(NotFoundUserException.class);

            //then
        }

        @Test
        @DisplayName("등록된 반려동물 목록을 PetResponse 목록으로 변환하여 반환한다.")
        void returnPetResponseList() throws NoSuchFieldException {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            Field idField = Pet.class.getDeclaredField("id");
            idField.setAccessible(true);
            AtomicLong id = new AtomicLong(1L);
            petList.forEach(pet -> {
                try {
                    idField.set(pet, id.getAndIncrement());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });

            when(petQuery.findByParent(user)).thenReturn(petList);
            List<PetResponse> expectedResult = petList.stream()
                .map(PetResponse::of)
                .collect(Collectors.toList());

            //when
            List<PetResponse> result = petService.getPetList(user.getUserId());

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
        }
    }

    @Nested
    @DisplayName("deletePet 메서드는")
    class DeletePet {
        User user = User.builder().build();
        Pet pet = Pet.builder()
            .name("루이")
            .petType(PetType.DOG)
            .introduction("루이는 비숑 2살 남자아이에요")
            .parent(user)
            .build();

        @BeforeEach
        void setup() throws NoSuchFieldException, IllegalAccessException {
            Field idField = Pet.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(pet, 1234L);
        }

        @Test
        @DisplayName("존재하지 않는 유저면 예외가 발생한다.")
        void IfNotExistsUserThenRaiseException() {
            //given
            when(userQuery.findByUserId(user.getUserId()))
                .thenReturn(Optional.empty());

            //when
            assertThatThrownBy(() -> petService.deletePet(user.getUserId(), pet.getId()))
                .isInstanceOf(NotFoundUserException.class);

            //then
        }

        @Test
        @DisplayName("존재하지 않는 반려동물이면 예외가 발생한다.")
        void IfNotExistsPetThenRaiseException() {
            //given
            when(userQuery.findByUserId(user.getUserId()))
                .thenReturn(Optional.of(user));
            when(petQuery.findByParentAndId(user, pet.getId()))
                .thenReturn(Optional.empty());

            //when
            assertThatThrownBy(() -> petService.deletePet(user.getUserId(), pet.getId()))
                .isInstanceOf(NotFoundPetException.class);

            //then
        }

        @Test
        @DisplayName("pet 엔티티를 petCommand의 delete 메서드에 인자로 전달한다.")
        void deletePetByPetCommand() {
            //given
            when(userQuery.findByUserId(user.getUserId()))
                .thenReturn(Optional.of(user));
            when(petQuery.findByParentAndId(user, pet.getId()))
                .thenReturn(Optional.of(pet));

            //when
            petService.deletePet(user.getUserId(), pet.getId());

            //then
            verify(petCommand).delete(pet);
        }
    }
}