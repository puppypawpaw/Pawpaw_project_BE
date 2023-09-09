package kr.co.pawpaw.mail.service;

import kr.co.pawpaw.mail.dto.SendEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailServiceJavaMailSenderImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    public void sendMail(final SendEmailRequest request) {
        MimeMessage mimeMessage = getMimeMessage(request);
        javaMailSender.send(mimeMessage);
    }

    private MimeMessage getMimeMessage(final SendEmailRequest request) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(request.getTo());
            mimeMessageHelper.setSubject(request.getSubject());
            mimeMessageHelper.setText(request.getText(), true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return mimeMessage;
    }
}