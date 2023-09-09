package kr.co.pawpaw.domainrdb.user.service.command;

import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCommandTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserCommand userCommand;

    @Test
    @DisplayName("save 메서드 테스트")
    void save() {
        //given
        User user = User.builder()
            .email("user-email")
            .nickname("user-nickname")
            .password("123")
            .build();

        when(userRepository.save(eq(user))).thenReturn(user);

        //when
        User newUser = userCommand.save(user);

        //then
        verify(userRepository).save(user);
        assertThat(newUser).isEqualTo(user);
    }
}