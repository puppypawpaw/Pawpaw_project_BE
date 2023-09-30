package kr.co.pawpaw.api.service.user;

import kr.co.pawpaw.api.dto.position.PositionRequest;
import kr.co.pawpaw.api.dto.user.UpdateUserPositionRequest;
import kr.co.pawpaw.api.dto.user.UpdateUserProfileRequest;
import kr.co.pawpaw.api.dto.user.UserResponse;
import kr.co.pawpaw.api.service.file.FileService;
import kr.co.pawpaw.api.util.user.UserUtil;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.position.Position;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.storage.domain.FileType;
import kr.co.pawpaw.domainrdb.storage.service.query.FileQuery;
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
    private MultipartFile multipartFile;
    @Mock
    private FileQuery fileQuery;
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
        private UpdateUserProfileRequest request = UpdateUserProfileRequest.builder()
            .briefIntroduction("한줄 소개입니다.")
            .nickname("닉네임 입니다.")
            .build();
        @Test
        @DisplayName("유저가 존재하지 않으면 예외가 발생한다.")
        void NotFoundUserException() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.empty());

            //when
            assertThatThrownBy(() -> userService.updateUserProfile(user.getUserId(), request))
                .isInstanceOf(NotFoundUserException.class);

            //then
        }

        @Test
        @DisplayName("유저의 닉네임과 한줄 소개를 변경한다.")
        void changeNicknameAndBriefIntroduction() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));

            //when
            userService.updateUserProfile(user.getUserId(), request);

            //then
            assertThat(request).usingRecursiveComparison().isEqualTo(user);
        }
    }

    @Nested
    @DisplayName("getUserDefaultImageUrl 메서드는")
    class GetUserDefaultImageUrl {
        File userDefaultImage = File.builder()
            .fileName("기본 이미지")
            .fileUrl("파일 URL")
            .build();

        @Test
        @DisplayName("fileRepository에 userDefaultImage파일이 없으면 null을 반환한다.")
        void returnNull() {
            //given
            when(fileQuery.findByFileName(UserUtil.getUserDefaultImageName())).thenReturn(Optional.empty());

            //when
            String result = userService.getUserDefaultImageUrl();

            //then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("fileRepository에 userDefaultImage파일이 존재하면 file의 url을 반환한다.")
        void returnUrl() {
            //given
            when(fileQuery.findByFileName(UserUtil.getUserDefaultImageName())).thenReturn(Optional.of(userDefaultImage));

            //when
            String result = userService.getUserDefaultImageUrl();

            //then
            assertThat(result).isEqualTo(userDefaultImage.getFileUrl());
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

    @Nested
    @DisplayName("updateUserPosition 메서드는")
    class UpdateUserPosition {
        private Position oldPosition = Position.builder()
            .name("old")
            .latitude(12.3)
            .longitude(12.4)
            .build();
        private Position newPosition = Position.builder()
            .name("new")
            .latitude(32.1)
            .longitude(32.2)
            .build();
        private User user = User.builder()
            .position(oldPosition)
            .build();
        private UpdateUserPositionRequest request = UpdateUserPositionRequest.builder()
            .position(PositionRequest.builder()
                .name(newPosition.getName())
                .latitude(newPosition.getLatitude())
                .longitude(newPosition.getLongitude())
                .build())
            .build();

        @Test
        @DisplayName("존재하지 않는 유저면 예외를 발생한다.")
        void NotFoundUserException() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.empty());

            //when
            assertThatThrownBy(() -> userService.updateUserPosition(user.getUserId(), request))
                .isInstanceOf(NotFoundUserException.class);

            //then
        }

        @Test
        @DisplayName("유저의 position을 request의 내용으로 변경한다.")
        void changeUserPositionByRequest() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));

            //when
            userService.updateUserPosition(user.getUserId(), request);

            //then
            assertThat(user.getPosition()).usingRecursiveComparison().isEqualTo(newPosition);
        }
    }
}