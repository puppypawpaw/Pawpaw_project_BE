package kr.co.pawpaw.domainrdb.sms.service.query;

import kr.co.pawpaw.domainrdb.sms.domain.SmsUsagePurpose;
import kr.co.pawpaw.domainrdb.sms.repository.SmsLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class SmsLogQuery {
    private final SmsLogRepository smsLogRepository;

    public Long getTodaySendCountByRecipientAndUsagePurpose(
        final String recipient,
        final SmsUsagePurpose usagePurpose
    ) {
        LocalDateTime now = LocalDateTime.now();
        return smsLogRepository.countByRecipientAndUsagePurposeAndCreatedDateBetween(
            recipient,
            usagePurpose,
            now.with(LocalTime.MIN),
            now.with(LocalTime.MAX)
        );
    }
}
