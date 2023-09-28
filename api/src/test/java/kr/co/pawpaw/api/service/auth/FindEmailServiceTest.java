package kr.co.pawpaw.api.service.auth;

import kr.co.pawpaw.api.dto.user.UserEmailResponse;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindEmailServiceTest {
    @Mock
    private UserQuery userQuery;
    @InjectMocks
    private FindEmailService findEmailService;

    @Test
    @DisplayName("getUserEmail 메서드 예외 테스트")
    void getUserEmailException() {
        //given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
            .name("user-name")
            .phoneNumber("user-phoneNumber")
            .email("email@liame.com")
            .createdDate(now)
            .modifiedDate(now)
            .build();

        String invalidName = "invalid-name";

        when(userQuery.findByNameAndPhoneNumber(invalidName, user.getPhoneNumber())).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> findEmailService.getUserEmail(invalidName, user.getPhoneNumber())).isInstanceOf(NotFoundUserException.class);

        //then
        verify(userQuery).findByNameAndPhoneNumber(invalidName, user.getPhoneNumber());
    }

    @Test
    @DisplayName("getUserEmail 메서드 정상작동 테스트")
    void getUserEmail() {
        //given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
            .name("user-name")
            .phoneNumber("user-phoneNumber")
            .email("email@liame.com")
            .createdDate(now)
            .modifiedDate(now)
            .build();

        UserEmailResponse resultExpected = UserEmailResponse.of("em***@liame.com", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        when(userQuery.findByNameAndPhoneNumber(user.getName(), user.getPhoneNumber())).thenReturn(Optional.of(user));

        //when
        UserEmailResponse result = findEmailService.getUserEmail(user.getName(), user.getPhoneNumber());

        //then
        verify(userQuery).findByNameAndPhoneNumber(user.getName(), user.getPhoneNumber());
        assertThat(result).usingRecursiveComparison().isEqualTo(resultExpected);
    }
}