package kr.co.pawpaw.domainrdb.user.service.query;

import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserQueryTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserQuery userQuery;

    private static User user = User.builder()
        .email("user-email")
        .nickname("user-nickname")
        .password("123")
        .build();

    @Test
    @DisplayName("existsById 메소드 테스트")
    void existsById() {
        //given
        when(userRepository.existsByEmail(eq(user.getEmail()))).thenReturn(true);
        //when
        boolean result = userQuery.existsById(user.getEmail());

        //then
        verify(userRepository).existsByEmail(user.getEmail());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("existsByUserIdAndRole 메소드 테스트")
    void existsByUserIdAndRole() {
        //given
        when(userRepository.existsByUserIdAndRole(eq(user.getUserId()), eq(user.getRole()))).thenReturn(true);
        //when
        boolean result = userQuery.existsByUserIdAndRole(user.getUserId(), user.getRole());

        //then
        verify(userRepository).existsByUserIdAndRole(user.getUserId(), user.getRole());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("findById 메소드 테스트")
    void findById() {
        //given
        when(userRepository.findByEmail(eq(user.getEmail()))).thenReturn(Optional.of(user));
        //when
        Optional<User> result = userQuery.findByEmail(user.getEmail());

        //then
        verify(userRepository).findByEmail(user.getEmail());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    @DisplayName("findByUserId 메소드 테스트")
    void findByUserId() {
        //given
        when(userRepository.findById(eq(user.getUserId()))).thenReturn(Optional.of(user));
        //when
        Optional<User> result = userQuery.findByUserId(user.getUserId());

        //then
        verify(userRepository).findById(user.getUserId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    @DisplayName("findByIdAndProvider 메소드 테스트")
    void findByIdAndProvider() {
        //given
        when(userRepository.findByEmailAndProvider(eq(user.getEmail()), eq(user.getProvider()))).thenReturn(Optional.of(user));
        //when
        Optional<User> result = userQuery.findByEmailAndProvider(user.getEmail(), user.getProvider());

        //then
        verify(userRepository).findByEmailAndProvider(user.getEmail(), user.getProvider());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(user);
    }
}