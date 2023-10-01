package kr.co.pawpaw.mysql.email.service.command;

import kr.co.pawpaw.mysql.email.domain.EmailLog;
import kr.co.pawpaw.mysql.email.repository.EmailLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailLogCommand {
    private final EmailLogRepository emailLogRepository;

    public EmailLog save(final EmailLog emailLog) {
        return emailLogRepository.save(emailLog);
    }
}
