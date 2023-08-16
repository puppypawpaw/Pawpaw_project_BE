package kr.co.pawpaw.domainrdb.user.domain.repository;

import kr.co.pawpaw.domainrdb.auth.OAuth2Provider;
import kr.co.pawpaw.domainrdb.user.domain.Role;
import kr.co.pawpaw.domainrdb.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("existsById 메소드 테스트")
    void existsById() {
        //given
        User user1 = User.builder()
            .id("user1")
            .build();
        User user2 = User.builder()
            .id("user2")
            .build();
        User user3 = User.builder()
            .id("user3")
            .build();

        userRepository.saveAll(List.of(user1, user3));

        //when
        boolean result1 = userRepository.existsById(user1.getId());
        boolean result2 = userRepository.existsById(user2.getId());
        boolean result3 = userRepository.existsById(user3.getId());

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
        assertThat(result3).isTrue();
    }

    @Test
    @DisplayName("existsByUserIdAndRole 메소드 테스트")
    void existsByUserIdAndRole() {
        //given
        User user1 = User.builder().build();
        User user2 = User.builder().build();
        User user3 = User.builder().build();

        userRepository.saveAll(List.of(user1, user3));

        //when
        boolean result1 = userRepository.existsByUserIdAndRole(user1.getUserId(), Role.USER);
        boolean result2 = userRepository.existsByUserIdAndRole(user2.getUserId(), Role.USER);
        boolean result3 = userRepository.existsByUserIdAndRole(user3.getUserId(), Role.USER);

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
        assertThat(result3).isTrue();
    }

    @Test
    @DisplayName("findById 메소드 테스트")
    void findById() {
        //given
        User user1 = User.builder()
            .id("user1")
            .build();
        User user2 = User.builder()
            .id("user2")
            .build();
        User user3 = User.builder()
            .id("user3")
            .build();

        userRepository.saveAll(List.of(user1, user3));

        //when
        Optional<User> result1 = userRepository.findById(user1.getId());
        Optional<User> result2 = userRepository.findById(user2.getId());
        Optional<User> result3 = userRepository.findById(user3.getId());

        //then
        assertThat(result1.isPresent()).isTrue();
        assertThat(result2.isPresent()).isFalse();
        assertThat(result3.isPresent()).isTrue();

        assertThat(result1.get().getUserId()).isEqualTo(user1.getUserId());
        assertThat(result1.get().getUserId()).isNotEqualTo(user2.getUserId());
        assertThat(result1.get().getUserId()).isNotEqualTo(user3.getUserId());

        assertThat(result3.get().getUserId()).isNotEqualTo(user1.getUserId());
        assertThat(result3.get().getUserId()).isNotEqualTo(user2.getUserId());
        assertThat(result3.get().getUserId()).isEqualTo(user3.getUserId());
    }

    @Test
    void findByIdAndProvider() {
        //given
        User user1 = User.builder()
            .id("user1")
            .provider(OAuth2Provider.KAKAO)
            .build();

        userRepository.save(user1);

        //when
        Optional<User> result1 = userRepository.findByIdAndProvider(user1.getId(), OAuth2Provider.KAKAO);
        Optional<User> result2 = userRepository.findByIdAndProvider(user1.getId(), OAuth2Provider.GOOGLE);
        Optional<User> result3 = userRepository.findByIdAndProvider(user1.getId(), OAuth2Provider.NAVER);

        //then
        assertThat(result1.isPresent()).isTrue();
        assertThat(result2.isPresent()).isFalse();
        assertThat(result3.isPresent()).isFalse();
    }
}