package kr.co.pawpaw.domainrdb.email.repository;

import kr.co.pawpaw.domainrdb.email.domain.EmailLog;
import kr.co.pawpaw.domainrdb.email.domain.EmailType;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.repository.UserRepository;
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
    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("저장 및 불러오기 테스트")
    void saveAndLoad() {
        //given
        User user = User.builder().build();

        userRepository.save(user);

        EmailLog emailLog = EmailLog.builder()
            .emailType(EmailType.CHANGE_PASSWORD)
            .recipient("recipient@mail.com")
            .sender(user)
            .text("text")
            .subject("subject")
            .build();

        //when
        emailLog = emailLogRepository.save(emailLog);
        Optional<EmailLog> result = emailLogRepository.findById(emailLog.getId());

        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(emailLog);
    }
}