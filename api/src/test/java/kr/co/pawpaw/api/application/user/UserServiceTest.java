package kr.co.pawpaw.api.application.user;

import kr.co.pawpaw.api.dto.pet.CreatePetRequest;
import kr.co.pawpaw.api.dto.pet.CreatePetResponse;
import kr.co.pawpaw.api.service.file.FileService;
import kr.co.pawpaw.api.dto.user.UserResponse;
import kr.co.pawpaw.api.service.user.UserService;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.pet.domain.Pet;
import kr.co.pawpaw.domainrdb.pet.domain.PetType;
import kr.co.pawpaw.domainrdb.pet.service.command.PetCommand;
import kr.co.pawpaw.domainrdb.position.Position;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("UserService의")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserQuery userQuery;
    @Mock
    private FileService fileService;
    @Mock
    private PetCommand petCommand;
    @Mock
    private EntityManager em;
    @Mock
    private MultipartFile multipartFile;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("whoAmI 메서드 테스트")
    void whoAmI() {
        //given
        Position position = Position.builder()
            .name("name")
            .latitude(36.8)
            .longitude(36.7)
            .build();
        File file = File.builder()
            .fileName(UUID.randomUUID().toString())
            .fileUrl("fileUrl")
            .build();
        User user = User.builder()
            .position(position)
            .userImage(file)
            .build();
        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));


        //when
        UserResponse userResponse = userService.whoAmI(user.getUserId());

        //then
        verify(userQuery).findByUserId(user.getUserId());
        assertThat(userResponse.getImageUrl()).isEqualTo(file.getFileUrl());
        assertThat(userResponse.getNickname()).isEqualTo(user.getNickname());
        assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());
        assertThat(userResponse.getRole()).isEqualTo(user.getRole());
        assertThat(userResponse.getPosition()).usingRecursiveComparison().isEqualTo(user.getPosition());
    }

    @Test
    @DisplayName("updateUserImage 기존 이미지 있을때 테스트")
    void updateUserImageAlreadyExists() {
        //given
        Position position = Position.builder()
            .name("name")
            .latitude(36.8)
            .longitude(36.7)
            .build();
        File file = File.builder()
            .fileName(UUID.randomUUID().toString())
            .build();
        User user = User.builder()
            .position(position)
            .userImage(file)
            .build();

        File newFile = File.builder()
            .fileName(UUID.randomUUID().toString())
            .build();

        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
        when(fileService.saveFileByMultipartFile(multipartFile, user.getUserId())).thenReturn(newFile);

        //when
        userService.updateUserImage(user.getUserId(), multipartFile);

        //then
        verify(fileService).deleteFileByName(file.getFileName());
    }

    @Test
    @DisplayName("updateUserImage 기존 이미지 없을때 테스트")
    void updateUserImageNotExists() {
        //given
        Position position = Position.builder()
            .name("name")
            .latitude(36.8)
            .longitude(36.7)
            .build();
        User user = User.builder()
            .position(position)
            .build();

        File newFile = File.builder()
            .fileName(UUID.randomUUID().toString())
            .build();

        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
        when(fileService.saveFileByMultipartFile(multipartFile, user.getUserId())).thenReturn(newFile);
        //when
        userService.updateUserImage(user.getUserId(), multipartFile);

        //then
        verify(fileService, times(0)).deleteFileByName(any());
    }

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
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.empty());

            //when
            assertThatThrownBy(() -> userService.createPet(user.getUserId(), createPetRequest))
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
            CreatePetResponse result = userService.createPet(user.getUserId(), createPetRequest);

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
        }
    }
}