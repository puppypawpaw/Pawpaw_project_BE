package kr.co.pawpaw.api.application.user;

import kr.co.pawpaw.api.application.file.FileService;
import kr.co.pawpaw.api.dto.user.UserResponse;
import kr.co.pawpaw.domainrdb.position.Position;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserImage;
import kr.co.pawpaw.domainrdb.user.service.command.UserImageCommand;
import kr.co.pawpaw.domainrdb.user.service.query.UserImageQuery;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserQuery userQuery;
    @Mock
    private UserImageCommand userImageCommand;
    @Mock
    private UserImageQuery userImageQuery;
    @Mock
    private FileService fileService;
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
        User user = User.builder()
            .position(position)
            .build();
        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
        File file = File.builder()
            .build();
        UserImage userImage = UserImage.builder()
            .user(user)
            .file(file)
            .build();

        String fileUrl = "fileUrl";

        when(userImageQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(userImage));
        when(fileService.getUrl(file.getFileName())).thenReturn(fileUrl);
        //when
        UserResponse userResponse = userService.whoAmI(user.getUserId());

        //then
        verify(userQuery).findByUserId(user.getUserId());
        verify(userImageQuery).findByUserId(user.getUserId());
        verify(fileService).getUrl(file.getFileName());
        assertThat(userResponse.getImageUrl()).isEqualTo(fileUrl);
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
        User user = User.builder()
            .position(position)
            .build();

        File file = File.builder()
            .build();
        UserImage userImage = UserImage.builder()
            .user(user)
            .file(file)
            .build();

        File newFile = File.builder().build();

        when(userImageQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(userImage));
        when(fileService.saveFileByMultipartFile(multipartFile, user.getUserId())).thenReturn(newFile);

        //when
        userService.updateUserImage(user.getUserId(), multipartFile);

        //then
        verify(fileService).deleteFileByName(file.getFileName());
        verify(userImageCommand, times(0)).save(any());
        assertThat(userImage.getFile()).isEqualTo(newFile);
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

        File newFile = File.builder().build();

        UserImage userImage = UserImage.builder()
            .user(user)
            .build();

        when(userImageQuery.findByUserId(user.getUserId())).thenReturn(Optional.empty());
        when(fileService.saveFileByMultipartFile(multipartFile, user.getUserId())).thenReturn(newFile);
        when(em.getReference(User.class, user.getUserId())).thenReturn(user);
        //when
        userService.updateUserImage(user.getUserId(), multipartFile);

        //then
        verify(fileService, times(0)).deleteFileByName(any());
        ArgumentCaptor<UserImage> userImageCaptor = ArgumentCaptor.forClass(UserImage.class);
        verify(userImageCommand, times(1)).save(userImageCaptor.capture());
        assertThat(userImageCaptor.getValue().getUser()).isEqualTo(user);
        assertThat(userImageCaptor.getValue().getFile()).isEqualTo(newFile);
    }
}