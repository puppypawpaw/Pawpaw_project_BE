package kr.co.pawpaw.domainrdb.user.service.command;

import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserImage;
import kr.co.pawpaw.domainrdb.user.repository.UserImageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserImageCommandTest {
    @Mock
    UserImageRepository userImageRepository;
    @InjectMocks
    UserImageCommand userImageCommand;

    @Test
    @DisplayName("save 메서드 테스트")
    void save() {
        //given
        UserImage userImage = UserImage.builder()
            .user(User.builder().build())
            .file(File.builder().build())
            .build();

        when(userImageRepository.save(eq(userImage))).thenReturn(userImage);

        //when
        UserImage savedImage = userImageCommand.save(userImage);

        //then
        verify(userImageRepository).save(userImage);
        assertThat(savedImage).usingRecursiveComparison().isEqualTo(userImage);
    }
}