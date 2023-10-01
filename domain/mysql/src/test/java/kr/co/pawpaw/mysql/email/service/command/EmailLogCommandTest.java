package kr.co.pawpaw.mysql.email.service.command;

import kr.co.pawpaw.mysql.email.domain.EmailLog;
import kr.co.pawpaw.mysql.email.domain.EmailType;
import kr.co.pawpaw.mysql.email.repository.EmailLogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailLogCommandTest {
    @Mock
    private EmailLogRepository emailLogRepository;
    @InjectMocks
    private EmailLogCommand emailLogCommand;

    @Test
    @DisplayName("save메서드는 emailLogRepository의 save 메서드를 호출한다.")
    void save() {
        //given
        EmailLog emailLog = EmailLog.builder()
            .emailType(EmailType.CHANGE_PASSWORD)
            .build();

        //when
        emailLogCommand.save(emailLog);

        //then
        verify(emailLogRepository).save(emailLog);
    }
}