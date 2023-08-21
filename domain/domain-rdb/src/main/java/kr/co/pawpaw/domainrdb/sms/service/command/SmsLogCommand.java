package kr.co.pawpaw.domainrdb.sms.service.command;

import kr.co.pawpaw.domainrdb.sms.domain.SmsLog;
import kr.co.pawpaw.domainrdb.sms.repository.SmsLogRepository;
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
