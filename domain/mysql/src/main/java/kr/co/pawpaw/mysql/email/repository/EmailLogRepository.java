package kr.co.pawpaw.mysql.email.repository;

import kr.co.pawpaw.mysql.email.domain.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
}
