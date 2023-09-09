package kr.co.pawpaw.domainrdb.email.repository;

import kr.co.pawpaw.domainrdb.email.domain.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
}
