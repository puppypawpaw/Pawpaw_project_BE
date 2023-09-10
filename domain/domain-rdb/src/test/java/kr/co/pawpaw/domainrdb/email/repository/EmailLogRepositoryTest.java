package kr.co.pawpaw.domainrdb.email.repository;

import kr.co.pawpaw.domainrdb.email.domain.EmailLog;
import kr.co.pawpaw.domainrdb.email.domain.EmailType;
import kr.co.pawpaw.domainrdb.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmailLogRepositoryTest {
    @Autowired
    private EmailLogRepository emailLogRepository;

    @BeforeEach
    void setup() {
        emailLogRepository.deleteAll();
    }

    @Test
    @DisplayName("저장 및 불러오기 테스트")
    void saveAndLoad() {
        //given
        User user = User.builder().build();

        EmailLog emailLog = EmailLog.builder()
            .emailType(EmailType.CHANGE_PASSWORD)
            .recipient("recipient@mail.com")
            .sender(user)
            .text("text")
            .subject("subject")
            .build();

        //when
        emailLogRepository.save(emailLog);
        Optional<EmailLog> result = emailLogRepository.findById(emailLog.getId());

        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(emailLog);
    }
}