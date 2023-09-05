package kr.co.pawpaw.api.application.sms;

import kr.co.pawpaw.api.config.property.VerificationProperties;
import kr.co.pawpaw.api.dto.sms.CheckVerificationCodeRequest;
import kr.co.pawpaw.api.dto.sms.CheckVerificationCodeResponse;
import kr.co.pawpaw.common.exception.sms.OutOfSmsLimitException;
import kr.co.pawpaw.domainrdb.sms.domain.SmsLog;
import kr.co.pawpaw.domainrdb.sms.domain.SmsUsagePurpose;
import kr.co.pawpaw.domainrdb.sms.service.command.SmsLogCommand;
import kr.co.pawpaw.domainrdb.sms.service.query.SmsLogQuery;
import kr.co.pawpaw.domainredis.auth.domain.VerificationCode;
import kr.co.pawpaw.domainredis.auth.domain.VerifiedPhoneNumber;
import kr.co.pawpaw.domainredis.auth.service.command.VerificationCodeCommand;
import kr.co.pawpaw.domainredis.auth.service.command.VerifiedPhoneNumberCommand;
import kr.co.pawpaw.domainredis.auth.service.query.VerificationCodeQuery;
import kr.co.pawpaw.feignClient.dto.Recipient;
import kr.co.pawpaw.feignClient.dto.SendSmsResponse;
import kr.co.pawpaw.feignClient.service.SmsFeignService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {
    @Mock
    private SmsFeignService smsFeignService;
    @Mock
    private VerificationCodeCommand verificationCodeCommand;
    @Mock
    private VerificationCodeQuery verificationCodeQuery;
    @Mock
    private SmsLogCommand smsLogCommand;
    @Mock
    private SmsLogQuery smsLogQuery;
    @Mock
    private VerifiedPhoneNumberCommand verifiedPhoneNumberCommand;
    @Mock
    private VerificationProperties verificationProperties;
    @InjectMocks
    private SmsService smsService;

    Recipient recipient = Recipient.builder()
        .to("01012345678")
        .build();

    SmsUsagePurpose smsUsagePurpose = SmsUsagePurpose.SIGN_UP;

    @Test
    @DisplayName("인증 코드 발송 제한 초과 예외 테스트")
    void sendVerificationCodeOutOfSmsLimitException() {
        //given
        when(smsLogQuery.getTodaySendCountByRecipientAndUsagePurpose(recipient.getTo(), smsUsagePurpose)).thenReturn(3L);

        //when
        assertThatThrownBy(() -> smsService.sendVerificationCode(recipient, smsUsagePurpose)).isInstanceOf(OutOfSmsLimitException.class);

        //then
        verify(smsLogQuery).getTodaySendCountByRecipientAndUsagePurpose(recipient.getTo(), smsUsagePurpose);
    }

    @Test
    @DisplayName("인증 코드 발송 정상 작동 테스트")
    void sendVerificationCode() {
        //given
        when(smsLogQuery.getTodaySendCountByRecipientAndUsagePurpose(recipient.getTo(), smsUsagePurpose)).thenReturn(3L);
        SendSmsResponse sendSmsResponse = new SendSmsResponse("requestId", "requestTime", "statusCode", "statusName");
        when(smsFeignService.sendSmsMessage(anyString(), eq(recipient))).thenReturn(sendSmsResponse);
        when(verificationProperties.getCodeLength()).thenReturn(6);
        //when
        smsService.sendVerificationCode(recipient, smsUsagePurpose);

        //then
        verify(smsLogQuery).getTodaySendCountByRecipientAndUsagePurpose(recipient.getTo(), smsUsagePurpose);
        ArgumentCaptor<VerificationCode> verificationCodeCaptor = ArgumentCaptor.forClass(VerificationCode.class);
        verify(verificationCodeCommand).save(verificationCodeCaptor.capture());
        assertThat(verificationCodeCaptor.getValue().getCode().length()).isEqualTo(6);
        ArgumentCaptor<SmsLog> smsLogCaptor = ArgumentCaptor.forClass(SmsLog.class);
        verify(smsLogCommand).save(smsLogCaptor.capture());
        assertThat(smsLogCaptor.getValue()).usingRecursiveComparison()
            .ignoringFields("cloudPlatform", "createdDate", "recipient", "modifiedDate", "usagePurpose", "id", "type", "title", "content")
            .isEqualTo(sendSmsResponse);
    }

    @Test
    @DisplayName("존재하지 않는 인증번호 인증 요청 예외 처리 테스트")
    void checkVerificationCodeNotExist() {
        //given
        CheckVerificationCodeRequest checkVerificationCodeRequest = CheckVerificationCodeRequest.builder()
            .phoneNumber("01012345678")
            .code("1234")
            .build();

        SmsUsagePurpose usagePurpose = SmsUsagePurpose.SIGN_UP;

        when(verificationCodeQuery.existsByPhoneNumberAndUsagePurposeAndCode(
            checkVerificationCodeRequest.getPhoneNumber(),
            usagePurpose.name(),
            checkVerificationCodeRequest.getCode()
        )).thenReturn(false);

        //when
        CheckVerificationCodeResponse result = smsService.checkVerificationCode(checkVerificationCodeRequest, usagePurpose);

        //then
        verify(verificationCodeQuery).existsByPhoneNumberAndUsagePurposeAndCode(
            checkVerificationCodeRequest.getPhoneNumber(),
            usagePurpose.name(),
            checkVerificationCodeRequest.getCode()
        );

        assertThat(result.isSuccess()).isFalse();
    }

    @Test
    @DisplayName("인증번호 인증 요청 정상 작동 테스트")
    void checkVerificationCode() {
        //given
        CheckVerificationCodeRequest checkVerificationCodeRequest = CheckVerificationCodeRequest.builder()
            .phoneNumber("01012345678")
            .code("1234")
            .build();

        SmsUsagePurpose usagePurpose = SmsUsagePurpose.SIGN_UP;

        when(verificationCodeQuery.existsByPhoneNumberAndUsagePurposeAndCode(
            checkVerificationCodeRequest.getPhoneNumber(),
            usagePurpose.name(),
            checkVerificationCodeRequest.getCode()
        )).thenReturn(true);

        VerifiedPhoneNumber expectedVerificationPhoneNumber = VerifiedPhoneNumber.builder()
            .phoneNumber(checkVerificationCodeRequest.getPhoneNumber())
            .usagePurpose(usagePurpose.name())
            .build();

        //when
        CheckVerificationCodeResponse result = smsService.checkVerificationCode(checkVerificationCodeRequest, usagePurpose);

        //then
        verify(verificationCodeQuery).existsByPhoneNumberAndUsagePurposeAndCode(
            checkVerificationCodeRequest.getPhoneNumber(),
            usagePurpose.name(),
            checkVerificationCodeRequest.getCode()
        );

        verify(verificationCodeCommand).deleteByPhoneNumberAndUsagePurpose(checkVerificationCodeRequest.getPhoneNumber(), usagePurpose.name());
        ArgumentCaptor<VerifiedPhoneNumber> verifiedPhoneNumberCaptor = ArgumentCaptor.forClass(VerifiedPhoneNumber.class);
        verify(verifiedPhoneNumberCommand).save(verifiedPhoneNumberCaptor.capture());
        assertThat(verifiedPhoneNumberCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedVerificationPhoneNumber);
        assertThat(result.isSuccess()).isTrue();
    }
}