package kr.co.pawpaw.mysql.sms.repository;

import kr.co.pawpaw.mysql.sms.domain.SmsLog;
import kr.co.pawpaw.mysql.sms.domain.SmsUsagePurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface SmsLogRepository extends JpaRepository<SmsLog, Long> {
    Long countByRecipientAndUsagePurposeAndCreatedDateBetween(
        final String recipient,
        final SmsUsagePurpose usagePurpose,
        final LocalDateTime start,
        final LocalDateTime end
    );
}
