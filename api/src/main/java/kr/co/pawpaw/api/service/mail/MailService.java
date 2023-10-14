package kr.co.pawpaw.api.service.mail;

import kr.co.pawpaw.mysql.email.domain.EmailLog;
import kr.co.pawpaw.mysql.email.domain.EmailType;
import kr.co.pawpaw.mysql.email.service.command.EmailLogCommand;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mail.dto.SendEmailRequest;
import kr.co.pawpaw.mail.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MailService {
    private final EmailService emailService;
    private final EmailLogCommand emailLogCommand;

    @Transactional(propagation = Propagation.MANDATORY)
    public void sendMail(
        final SendEmailRequest request,
        final EmailType emailType,
        final User user
    ) {
        // email 전송
        emailService.sendMail(request);

        // email 전송 로그 db에 남기기
        createEmailLog(request, emailType, user);
    }

    private void createEmailLog(
        final SendEmailRequest request,
        final EmailType emailType,
        final User user
    ) {
        emailLogCommand.save(EmailLog.builder()
            .text(request.getText())
            .emailType(emailType)
            .subject(request.getSubject())
            .recipient(request.getTo())
            .sender(user)
            .build());
    }
}
