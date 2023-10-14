package kr.co.pawpaw.mail.service;

import kr.co.pawpaw.mail.dto.SendEmailRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceJavaMailSenderImplTest {
    @Mock
    private JavaMailSender javaMailSender;
    @InjectMocks
    private EmailServiceJavaMailSenderImpl emailServiceJavaMailSender;

    @Test
    @DisplayName("sendMail메서드는 javaMailSender의 send메서드를 호출한다.")
    void sendMail() {
        //given
        SendEmailRequest request = SendEmailRequest.builder()
            .to("recipient@example.com")
            .subject("Test Subject")
            .text("Test Email Body")
            .build();

        MimeMessage expectedMimeMessage = getExpectedMimeMessage(request);

        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        //when
        emailServiceJavaMailSender.sendMail(request);

        //then
        ArgumentCaptor<MimeMessage> mimeMessageArgumentCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(javaMailSender).send(mimeMessageArgumentCaptor.capture());
        assertThat(mimeMessageArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedMimeMessage);
    }

    private static MimeMessage getExpectedMimeMessage(SendEmailRequest request) {
        MimeMessage expectedMimeMessage = new MimeMessage((Session) null);

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(expectedMimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(request.getTo());
            mimeMessageHelper.setSubject(request.getSubject());
            mimeMessageHelper.setText(request.getText(), true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return expectedMimeMessage;
    }
}