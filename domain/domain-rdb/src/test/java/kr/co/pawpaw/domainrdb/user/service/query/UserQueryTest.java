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
        .name("user-name")
        .nickname("user-nickname")
        .password("123")
        .build();

    @Test
    @DisplayName("existsByEmailAndProvider 메소드 테스트")
    void existsByEmailAndProvider() {
        //given
        when(userRepository.existsByEmailAndProvider(eq(user.getEmail()), eq(user.getProvider()))).thenReturn(true);
        //when
        boolean result = userQuery.existsByEmailAndProvider(user.getEmail(), user.getProvider());

        //then
        verify(userRepository).existsByEmailAndProvider(user.getEmail(), user.getProvider());
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
    @DisplayName("findByEmailAndProvider 메소드 테스트")
    void findByEmailAndProvider() {
        //given
        when(userRepository.findByEmailAndProvider(eq(user.getEmail()), eq(user.getProvider()))).thenReturn(Optional.of(user));
        //when
        Optional<User> result = userQuery.findByEmailAndProvider(user.getEmail(), user.getProvider());

        //then
        verify(userRepository).findByEmailAndProvider(user.getEmail(), user.getProvider());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    @DisplayName("existsByPhoneNumber 메소드 테스트")
    void existsByPhoneNumber() {
        //given
        when(userRepository.existsByPhoneNumber(eq(user.getPhoneNumber()))).thenReturn(true);

        //when
        boolean result = userQuery.existsByPhoneNumber(user.getPhoneNumber());

        //then
        verify(userRepository).existsByPhoneNumber(user.getPhoneNumber());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("findByNameAndPhoneNumber 메소드 테스트")
    void findByNameAndPhoneNumber() {
        //given
        when(userRepository.findByNameAndPhoneNumber(user.getName(), user.getPhoneNumber())).thenReturn(Optional.of(user));

        //when
        Optional<User> result = userQuery.findByNameAndPhoneNumber(user.getName(), user.getPhoneNumber());

        //then
        verify(userRepository).findByNameAndPhoneNumber(user.getName(), user.getPhoneNumber());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(user);
    }
}