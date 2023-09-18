package kr.co.pawpaw.api.service.mail;

import kr.co.pawpaw.api.service.mail.MailService;
import kr.co.pawpaw.domainrdb.email.domain.EmailLog;
import kr.co.pawpaw.domainrdb.email.domain.EmailType;
import kr.co.pawpaw.domainrdb.email.service.command.EmailLogCommand;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.mail.dto.SendEmailRequest;
import kr.co.pawpaw.mail.service.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {
    @Mock
    private EmailService emailService;
    @Mock
    private EmailLogCommand emailLogCommand;
    @InjectMocks
    private MailService mailService;

    @Test
    @DisplayName("sendMail메서드는 emailService의 sendMail메서드와 emailLogCommand의 save메서드를 호출함")
    void sendMail() {
        //given
        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
            .to("to@mail.com")
            .subject("subject")
            .text("text")
            .build();

        EmailType emailType = EmailType.CHANGE_PASSWORD;
        User user = User.builder()
            .build();

        EmailLog expectedEmailLog = EmailLog.builder()
            .sender(user)
            .recipient(sendEmailRequest.getTo())
            .subject(sendEmailRequest.getSubject())
            .emailType(emailType)
            .text(sendEmailRequest.getText())
            .build();

        //when
        mailService.sendMail(sendEmailRequest, emailType, user);

        //then
        verify(emailService).sendMail(sendEmailRequest);
        ArgumentCaptor<EmailLog> emailLogArgumentCaptor = ArgumentCaptor.forClass(EmailLog.class);
        verify(emailLogCommand).save(emailLogArgumentCaptor.capture());
        assertThat(emailLogArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedEmailLog);
    }
}