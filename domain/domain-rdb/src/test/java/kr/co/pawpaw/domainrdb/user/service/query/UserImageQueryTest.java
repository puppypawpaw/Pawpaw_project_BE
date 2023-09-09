package kr.co.pawpaw.domainrdb.user.service.query;

import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.domain.UserImage;
import kr.co.pawpaw.domainrdb.user.repository.UserImageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserImageQueryTest {
    @Mock
    UserImageRepository userImageRepository;
    @InjectMocks
    UserImageQuery userImageQuery;

    @Test
    @DisplayName("findByUserId 메서드 테스트")
    void findByUserId() {
        //given
        UserId userId = UserId.create();
        UserImage userImage = UserImage.builder()
            .file(File.builder().build())
            .user(User.builder().build())
            .build();

        when(userImageRepository.findByUserUserId(userId)).thenReturn(Optional.of(userImage));

        //when
        Optional<UserImage> result = userImageQuery.findByUserId(userId);

        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(userImage);
    }
}