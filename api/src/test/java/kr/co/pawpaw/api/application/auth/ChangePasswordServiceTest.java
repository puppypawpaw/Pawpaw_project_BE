package kr.co.pawpaw.api.service.auth;

import kr.co.pawpaw.api.service.auth.ChangePasswordService;
import kr.co.pawpaw.api.service.mail.MailService;
import kr.co.pawpaw.api.config.property.MailProperties;
import kr.co.pawpaw.api.dto.auth.ChangePasswordMailRequest;
import kr.co.pawpaw.api.dto.auth.ChangePasswordRequest;
import kr.co.pawpaw.api.util.mail.ChangePasswordMailContent;
import kr.co.pawpaw.common.exception.auth.NotFoundChangePasswordTempKeyException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.email.domain.EmailType;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import kr.co.pawpaw.domainredis.auth.domain.ChangePasswordTempKey;
import kr.co.pawpaw.domainredis.auth.service.command.ChangePasswordTempKeyCommand;
import kr.co.pawpaw.domainredis.auth.service.query.ChangePasswordTempKeyQuery;
import kr.co.pawpaw.mail.dto.SendEmailRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangePasswordServiceTest {
    @Mock
    private UserQuery userQuery;
    @Mock
    private MailProperties mailProperties;
    @Mock
    private ChangePasswordTempKeyCommand changePasswordTempKeyCommand;
    @Mock
    private ChangePasswordTempKeyQuery changePasswordTempKeyQuery;
    @Mock
    private MailService mailService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private ChangePasswordService changePasswordService;

    private ChangePasswordMailRequest changePasswordMailRequest = ChangePasswordMailRequest.builder()
        .email("test@email.com")
        .name("testName")
        .build();

    private User user = User.builder()
        .email("user@email.com")
        .name("username")
        .build();

    @Test
    @DisplayName("sendChangePasswordMail 메서드 존재하지 않는 유저 예외 테스트")
    void sendChangePasswordMailNotFoundUserException() {
        //given
        when(userQuery.findByNameAndEmailAndProvider(changePasswordMailRequest.getName(), changePasswordMailRequest.getEmail(), null)).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> changePasswordService.sendChangePasswordMail(changePasswordMailRequest)).isInstanceOf(NotFoundUserException.class);

        //then
        verify(userQuery).findByNameAndEmailAndProvider(changePasswordMailRequest.getName(), changePasswordMailRequest.getEmail(), null);
    }

    @Test
    @DisplayName("sendChangePasswordMail 메서드 정상작동 테스트")
    void sendChangePasswordMail() {
        //given
        when(userQuery.findByNameAndEmailAndProvider(changePasswordMailRequest.getName(), changePasswordMailRequest.getEmail(), null)).thenReturn(Optional.of(user));

        ChangePasswordTempKey expectedTempKey = ChangePasswordTempKey.builder()
            .userId(user.getUserId().getValue())
            .build();

        when(changePasswordTempKeyCommand.save(any(ChangePasswordTempKey.class))).thenReturn(expectedTempKey);

        ChangePasswordMailContent changePasswordMailContent = ChangePasswordMailContent.builder()
            .linkUrl("linkUrl")
            .build();

        SendEmailRequest expectedSendEmailRequest = SendEmailRequest.builder()
            .to(user.getEmail())
            .subject(changePasswordMailContent.getSubject())
            .text("anyString")
            .build();

        //when
        changePasswordService.sendChangePasswordMail(changePasswordMailRequest);

        //then
        verify(userQuery).findByNameAndEmailAndProvider(changePasswordMailRequest.getName(), changePasswordMailRequest.getEmail(), null);
        ArgumentCaptor<ChangePasswordTempKey> changePasswordTempKeyArgumentCaptor = ArgumentCaptor.forClass(ChangePasswordTempKey.class);
        verify(changePasswordTempKeyCommand).save(changePasswordTempKeyArgumentCaptor.capture());
        assertThat(changePasswordTempKeyArgumentCaptor.getValue()).usingRecursiveComparison().ignoringFields("key").isEqualTo(expectedTempKey);
        ArgumentCaptor<SendEmailRequest> sendEmailRequestArgumentCaptor = ArgumentCaptor.forClass(SendEmailRequest.class);
        verify(mailService).sendMail(sendEmailRequestArgumentCaptor.capture(), eq(EmailType.CHANGE_PASSWORD), eq(user));
        assertThat(sendEmailRequestArgumentCaptor.getValue()).usingRecursiveComparison().ignoringFields("text").isEqualTo(expectedSendEmailRequest);
    }

    @Test
    @DisplayName("changePassword 메서드 존재하지 않는 임시키 예외 테스트")
    void changePasswordNotFoundTempKey() {
        //given
        ChangePasswordTempKey changePasswordTempKey = ChangePasswordTempKey.builder()
            .userId(user.getUserId().getValue())
            .build();

        ChangePasswordRequest request = ChangePasswordRequest.builder()
            .key(changePasswordTempKey.getKey())
            .password("1234")
            .build();

        when(changePasswordTempKeyQuery.findChangePasswordTempKeyByKey(changePasswordTempKey.getKey())).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> changePasswordService.changePassword(request)).isInstanceOf(NotFoundChangePasswordTempKeyException.class);

        //then
        verify(changePasswordTempKeyQuery).findChangePasswordTempKeyByKey(changePasswordTempKey.getKey());
    }

    @Test
    @DisplayName("changePassword 메서드 존재하지 않는 유저 예외 테스트")
    void changePasswordNotFoundUser() {
        //given
        ChangePasswordTempKey changePasswordTempKey = ChangePasswordTempKey.builder()
            .userId(user.getUserId().getValue())
            .build();

        ChangePasswordRequest request = ChangePasswordRequest.builder()
            .key(changePasswordTempKey.getKey())
            .password("1234")
            .build();

        when(changePasswordTempKeyQuery.findChangePasswordTempKeyByKey(changePasswordTempKey.getKey())).thenReturn(Optional.of(changePasswordTempKey));
        when(userQuery.findByUserId(UserId.of(changePasswordTempKey.getUserId()))).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> changePasswordService.changePassword(request)).isInstanceOf(NotFoundUserException.class);

        //then
        verify(changePasswordTempKeyQuery).findChangePasswordTempKeyByKey(changePasswordTempKey.getKey());
        verify(userQuery).findByUserId(UserId.of(changePasswordTempKey.getUserId()));
    }

    @Test
    @DisplayName("changePassword 메서드 유저 비밀번호 업데이트 테스트")
    void changePasswordUpdateUserPassword() {
        //given
        ChangePasswordTempKey changePasswordTempKey = ChangePasswordTempKey.builder()
            .userId(user.getUserId().getValue())
            .build();

        ChangePasswordRequest request = ChangePasswordRequest.builder()
            .key(changePasswordTempKey.getKey())
            .password("12345")
            .build();

        String encodedPassword = "encodedPassword";

        when(changePasswordTempKeyQuery.findChangePasswordTempKeyByKey(changePasswordTempKey.getKey())).thenReturn(Optional.of(changePasswordTempKey));
        when(userQuery.findByUserId(UserId.of(changePasswordTempKey.getUserId()))).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);

        //when
        changePasswordService.changePassword(request);

        //then
        verify(changePasswordTempKeyQuery).findChangePasswordTempKeyByKey(changePasswordTempKey.getKey());
        verify(userQuery).findByUserId(UserId.of(changePasswordTempKey.getUserId()));
        assertThat(user.getPassword()).isEqualTo(encodedPassword);
    }

    @Test
    @DisplayName("changePassword 메서드 임시 키 삭제 테스트")
    void changePasswordDeleteTempKey() {
        //given
        ChangePasswordTempKey changePasswordTempKey = ChangePasswordTempKey.builder()
            .userId(user.getUserId().getValue())
            .build();

        ChangePasswordRequest request = ChangePasswordRequest.builder()
            .key(changePasswordTempKey.getKey())
            .password("12345")
            .build();

        String encodedPassword = "encodedPassword";

        when(changePasswordTempKeyQuery.findChangePasswordTempKeyByKey(changePasswordTempKey.getKey())).thenReturn(Optional.of(changePasswordTempKey));
        when(userQuery.findByUserId(UserId.of(changePasswordTempKey.getUserId()))).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);

        //when
        changePasswordService.changePassword(request);

        //then
        verify(changePasswordTempKeyQuery).findChangePasswordTempKeyByKey(changePasswordTempKey.getKey());
        verify(userQuery).findByUserId(UserId.of(changePasswordTempKey.getUserId()));
        assertThat(user.getPassword()).isEqualTo(encodedPassword);
        verify(changePasswordTempKeyCommand).delete(changePasswordTempKey);
    }
}