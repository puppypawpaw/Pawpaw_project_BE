package kr.co.pawpaw.domainrdb.sms.service.query;

import kr.co.pawpaw.domainrdb.sms.domain.SmsUsagePurpose;
import kr.co.pawpaw.domainrdb.sms.repository.SmsLogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmsLogQueryTest {
    @Mock
    private SmsLogRepository smsLogRepository;
    @InjectMocks
    private SmsLogQuery smsLogQuery;

    @Test
    @DisplayName("getTodaySendCountByRecipientAndUsagePurpose 메서드 smsLogRepository countByRecipientAndUsagePurposeAndCreatedDateBetween 메서드 호출 테스트")
    void getTodaySendCountByRecipientAndUsagePurpose() {
        //given
        String recipient = "01012345678";
        SmsUsagePurpose usagePurpose = SmsUsagePurpose.SIGN_UP;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.with(LocalTime.MIN);
        LocalDateTime end = now.with(LocalTime.MAX);
        Long returnVal = 42L;

        when(smsLogRepository.countByRecipientAndUsagePurposeAndCreatedDateBetween(
            recipient,
            usagePurpose,
            start,
            end
        )).thenReturn(returnVal);

        //when
        Long result = smsLogQuery.getTodaySendCountByRecipientAndUsagePurpose(recipient, usagePurpose);

        //then
        verify(smsLogRepository).countByRecipientAndUsagePurposeAndCreatedDateBetween(recipient, usagePurpose, start, end);
        assertThat(result).isEqualTo(returnVal);
    }
}