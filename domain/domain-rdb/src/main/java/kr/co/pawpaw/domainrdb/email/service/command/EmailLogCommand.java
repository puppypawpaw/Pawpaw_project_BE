package kr.co.pawpaw.domainrdb.email.service.command;

import kr.co.pawpaw.domainrdb.email.domain.EmailLog;
import kr.co.pawpaw.domainrdb.email.repository.EmailLogRepository;
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
