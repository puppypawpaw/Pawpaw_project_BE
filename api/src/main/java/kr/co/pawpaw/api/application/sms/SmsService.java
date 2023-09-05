package kr.co.pawpaw.api.application.sms;

import kr.co.pawpaw.api.config.property.VerificationProperties;
import kr.co.pawpaw.api.dto.sms.CheckVerificationCodeRequest;
import kr.co.pawpaw.api.dto.sms.CheckVerificationCodeResponse;
import kr.co.pawpaw.common.exception.sms.OutOfSmsLimitException;
import kr.co.pawpaw.domainrdb.sms.domain.SmsCloudPlatform;
import kr.co.pawpaw.domainrdb.sms.domain.SmsLog;
import kr.co.pawpaw.domainrdb.sms.domain.SmsType;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class SmsService {
    private final SmsFeignService smsFeignService;
    private final VerificationCodeCommand verificationCodeCommand;
    private final VerificationCodeQuery verificationCodeQuery;
    private final SmsLogCommand smsLogCommand;
    private final SmsLogQuery smsLogQuery;
    private final VerifiedPhoneNumberCommand verifiedPhoneNumberCommand;
    private final VerificationProperties verificationProperties;

    @Transactional
    public void sendVerificationCode(
        final Recipient recipient,
        final SmsUsagePurpose usagePurpose
    ) {
        checkSmsLimitPerDay(recipient, usagePurpose);
        recipient.deleteToHyphen();
        VerificationCode code = getVerificationCode(recipient.getTo(), usagePurpose);
        String content = getVerificationContent(code.getCode());

        SendSmsResponse response = smsFeignService.sendSmsMessage(content, recipient);
        SmsLog smsLog = createSmsLog(SmsType.SMS, usagePurpose, recipient, content, response);

        verificationCodeCommand.save(code);
        smsLogCommand.save(smsLog);
    }

    public CheckVerificationCodeResponse checkVerificationCode(
        final CheckVerificationCodeRequest request,
        final SmsUsagePurpose usagePurpose
    ) {
        if (notExist(request, usagePurpose)) {
            return CheckVerificationCodeResponse.of(false);
        }

        deleteVerificationCode(request, usagePurpose);
        saveVerifiedPhoneNumber(request.getPhoneNumber(), usagePurpose.name());

        return CheckVerificationCodeResponse.of(true);
    }

    private void checkSmsLimitPerDay(
        final Recipient recipient,
        final SmsUsagePurpose usagePurpose
    ) {
        Long sendCount = smsLogQuery.getTodaySendCountByRecipientAndUsagePurpose(
            recipient.getTo(),
            usagePurpose
        );

        if (sendCount >= usagePurpose.getLimitPerDay()) {
            throw new OutOfSmsLimitException();
        }
    }

    private void deleteVerificationCode(CheckVerificationCodeRequest request, SmsUsagePurpose usagePurpose) {
        verificationCodeCommand.deleteByPhoneNumberAndUsagePurpose(request.getPhoneNumber(), usagePurpose.name());
    }

    private boolean notExist(final CheckVerificationCodeRequest request, final SmsUsagePurpose usagePurpose) {
        return !verificationCodeQuery.existsByPhoneNumberAndUsagePurposeAndCode(
            request.getPhoneNumber(),
            usagePurpose.name(),
            request.getCode()
        );
    }

    private static SmsLog createSmsLog(
        final SmsType smsType,
        final SmsUsagePurpose usagePurpose,
        final Recipient recipient,
        final String content,
        final SendSmsResponse response
    ) {
        return SmsLog.builder()
            .type(smsType)
            .usagePurpose(usagePurpose)
            .cloudPlatform(SmsCloudPlatform.NAVER)
            .recipient(recipient.getTo())
            .content(content)
            .requestId(response.getRequestId())
            .statusCode(response.getStatusCode())
            .statusName(response.getStatusName())
            .build();
    }

    private void saveVerifiedPhoneNumber(final String phoneNumber, final String usagePurpose) {
        VerifiedPhoneNumber verifiedPhoneNumber = VerifiedPhoneNumber.builder()
            .phoneNumber(phoneNumber)
            .usagePurpose(usagePurpose)
            .build();
        verifiedPhoneNumberCommand.save(verifiedPhoneNumber);
    }

    private VerificationCode getVerificationCode(
        final String phoneNumber,
        final SmsUsagePurpose usagePurpose
    ) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(verificationProperties.getCodeLength());

        for (int i = 0; i < verificationProperties.getCodeLength(); ++i) {
            sb.append(Math.abs(random.nextInt()) % 10);
        }

        return VerificationCode.builder()
            .phoneNumber(phoneNumber)
            .usagePurpose(usagePurpose.name())
            .code(sb.toString())
            .build();
    }

    private String getVerificationContent(final String code) {
        return "[PawPaw] 인증번호[" + code + "]를 입력해주세요.";
    }
}
