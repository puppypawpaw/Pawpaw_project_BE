package kr.co.pawpaw.mysql.user.repository;

import kr.co.pawpaw.mysql.user.domain.OAuth2Provider;
import kr.co.pawpaw.mysql.user.domain.Role;
import kr.co.pawpaw.mysql.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("existsById 메서드 테스트")
    void existsById() {
        //given
        User user1 = User.builder()
            .email("user1")
            .build();
        User user2 = User.builder()
            .email("user2")
            .build();
        User user3 = User.builder()
            .email("user3")
            .build();

        userRepository.saveAll(List.of(user1, user3));

        //when
        boolean result1 = userRepository.existsByEmail(user1.getEmail());
        boolean result2 = userRepository.existsByEmail(user2.getEmail());
        boolean result3 = userRepository.existsByEmail(user3.getEmail());

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
        assertThat(result3).isTrue();
    }

    @Test
    @DisplayName("existsByEmailAndProvider 메서드 테스트")
    void existsByEmailAndProvider() {
        //given
        User user1 = User.builder()
            .email("user1")
            .provider(OAuth2Provider.KAKAO)
            .build();

        userRepository.save(user1);

        //when
        boolean result1 = userRepository.existsByEmailAndProvider(user1.getEmail(), OAuth2Provider.KAKAO);
        boolean result2 = userRepository.existsByEmailAndProvider(user1.getEmail(), OAuth2Provider.GOOGLE);
        boolean result3 = userRepository.existsByEmailAndProvider(user1.getEmail(), OAuth2Provider.NAVER);

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
        assertThat(result3).isFalse();
    }

    @Test
    @DisplayName("existsByUserIdAndRole 메서드 테스트")
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
    @DisplayName("findById 메서드 테스트")
    void findById() {
        //given
        User user1 = User.builder()
            .email("user1")
            .build();
        User user2 = User.builder()
            .email("user2")
            .build();
        User user3 = User.builder()
            .email("user3")
            .build();

        userRepository.saveAll(List.of(user1, user3));

        //when
        Optional<User> result1 = userRepository.findByEmail(user1.getEmail());
        Optional<User> result2 = userRepository.findByEmail(user2.getEmail());
        Optional<User> result3 = userRepository.findByEmail(user3.getEmail());

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
            .email("user1")
            .provider(OAuth2Provider.KAKAO)
            .build();

        userRepository.save(user1);

        //when
        Optional<User> result1 = userRepository.findByEmailAndProvider(user1.getEmail(), OAuth2Provider.KAKAO);
        Optional<User> result2 = userRepository.findByEmailAndProvider(user1.getEmail(), OAuth2Provider.GOOGLE);
        Optional<User> result3 = userRepository.findByEmailAndProvider(user1.getEmail(), OAuth2Provider.NAVER);

        //then
        assertThat(result1.isPresent()).isTrue();
        assertThat(result2.isPresent()).isFalse();
        assertThat(result3.isPresent()).isFalse();
    }

    @Test
    @DisplayName("findByNameAndPhoneNumber 메서드 테스트")
    void findByNameAndPhoneNumber() {
        //given
        User user1 = User.builder()
            .email("user1")
            .name("user1-name")
            .phoneNumber("user1-phoneNumber")
            .provider(OAuth2Provider.KAKAO)
            .build();

        user1 = userRepository.save(user1);

        //when
        Optional<User> result = userRepository.findByNameAndPhoneNumber(user1.getName(), user1.getPhoneNumber());

        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(user1);
    }

    @Test
    @DisplayName("findByNameAndEmailAndProvider 메서드 테스트")
    void findByNameAndEmailAndProvider() {
        //given
        User user = User.builder()
            .email("user1@gmail.com")
            .name("user1-name")
            .phoneNumber("user1-phoneNumber")
            .provider(OAuth2Provider.NAVER)
            .build();

        user = userRepository.save(user);

        //when
        Optional<User> result = userRepository.findByNameAndEmailAndProvider(user.getName(), user.getEmail(), user.getProvider());

        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(user);
    }
}