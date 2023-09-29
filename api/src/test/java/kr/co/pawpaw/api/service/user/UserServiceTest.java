package kr.co.pawpaw.api.service.user;

import kr.co.pawpaw.api.dto.user.UpdateUserRequest;
import kr.co.pawpaw.api.dto.user.UserResponse;
import kr.co.pawpaw.api.service.file.FileService;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.pet.service.command.PetCommand;
import kr.co.pawpaw.domainrdb.pet.service.query.PetQuery;
import kr.co.pawpaw.domainrdb.position.Position;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.storage.domain.FileType;
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
    private PetQuery petQuery;
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

    @Nested
    @DisplayName("updateUserImage 메서드는")
    class updateUserImage {
        Position position = Position.builder()
            .name("name")
            .latitude(36.8)
            .longitude(36.7)
            .build();
        File defaultFile = File.builder()
            .fileName(UUID.randomUUID().toString())
            .type(FileType.DEFAULT)
            .build();
        File customFile = File.builder()
            .fileName(UUID.randomUUID().toString())
            .type(FileType.CUSTOM)
            .build();
        User user = User.builder()
            .position(position)
            .build();

        File newFile = File.builder()
            .fileName(UUID.randomUUID().toString())
            .build();

        @Test
        @DisplayName("유저가 존재하지 않으면 예외가 발생한다.")
        void NotFoundUserException() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.empty());

            //when
            assertThatThrownBy(() -> userService.updateUserImage(user.getUserId(), multipartFile))
                .isInstanceOf(NotFoundUserException.class);

            //then
        }

        @Test
        @DisplayName("기본 이미지는 이미지를 삭제하지 않고 업데이트만 수행한다.")
        void notDeleteDefaultImageFile() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            when(fileService.saveFileByMultipartFile(multipartFile, user.getUserId())).thenReturn(newFile);
            user.updateImage(defaultFile);

            //when
            userService.updateUserImage(user.getUserId(), multipartFile);

            //then
            verify(fileService, times(0)).deleteFileByName(any());
            assertThat(user.getUserImage()).usingRecursiveComparison().isEqualTo(newFile);
        }

        @Test
        @DisplayName("기본이 아닌 이미지는 이미지를 삭제하지 않고 업데이트만 수행한다.")
        void deleteDefaultImageFile() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            when(fileService.saveFileByMultipartFile(multipartFile, user.getUserId())).thenReturn(newFile);
            user.updateImage(customFile);

            //when
            userService.updateUserImage(user.getUserId(), multipartFile);

            //then
            verify(fileService).deleteFileByName(customFile.getFileName());
            assertThat(user.getUserImage()).usingRecursiveComparison().isEqualTo(newFile);
        }
    }

    @Nested
    @DisplayName("updateUser 메서드는")
    class UpdateUser {
        private User user = User.builder().build();
        private UpdateUserRequest request = UpdateUserRequest.builder()
            .briefIntroduction("한줄 소개입니다.")
            .nickname("닉네임 입니다.")
            .build();
        @Test
        @DisplayName("유저가 존재하지 않으면 예외가 발생한다.")
        void NotFoundUserException() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.empty());

            //when
            assertThatThrownBy(() -> userService.updateUser(user.getUserId(), request))
                .isInstanceOf(NotFoundUserException.class);

            //then
        }

        @Test
        @DisplayName("유저의 닉네임과 한줄 소개를 변경한다.")
        void changeNicknameAndBriefIntroduction() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));

            //when
            userService.updateUser(user.getUserId(), request);

            //then
            assertThat(request).usingRecursiveComparison().isEqualTo(user);
        }
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
}