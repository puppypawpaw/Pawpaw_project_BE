package kr.co.pawpaw.mysql.sms.service.command;

import kr.co.pawpaw.mysql.sms.domain.SmsLog;
import kr.co.pawpaw.mysql.sms.repository.SmsLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsLogCommand {
    private final SmsLogRepository smsLogRepository;

    public SmsLog save(final SmsLog smsLog) {
        return smsLogRepository.save(smsLog);
    }
}
