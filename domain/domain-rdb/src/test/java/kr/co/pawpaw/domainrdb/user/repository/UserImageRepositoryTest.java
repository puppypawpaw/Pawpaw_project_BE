package kr.co.pawpaw.domainrdb.user.repository;

import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserImageRepositoryTest {
    @Autowired
    UserImageRepository userImageRepository;

    @BeforeEach
    void setup() {
        userImageRepository.deleteAll();
    }

    @Test
    @DisplayName("저장 및 불러오기 테스트")
    void saveAndLoadTest() {
        //given
        User user = User.builder().build();
        File file = File.builder().build();
        UserImage userImage = UserImage.builder()
            .user(user)
            .file(file)
            .build();

        //when
        userImageRepository.save(userImage);
        Optional<UserImage> savedUserImage = userImageRepository.findById(userImage.getId());

        //then
        assertThat(savedUserImage.isPresent()).isTrue();
        assertThat(savedUserImage.get()).usingRecursiveComparison().isEqualTo(userImage);
    }
}