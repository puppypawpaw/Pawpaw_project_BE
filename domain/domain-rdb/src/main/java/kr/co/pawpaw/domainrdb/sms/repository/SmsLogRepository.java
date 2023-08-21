package kr.co.pawpaw.domainrdb.sms.repository;

import kr.co.pawpaw.domainrdb.sms.domain.SmsLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsLogRepository extends JpaRepository<SmsLog, Long> {
}
