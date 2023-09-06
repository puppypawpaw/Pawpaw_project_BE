package kr.co.pawpaw.api.application.sms;

import kr.co.pawpaw.api.config.property.VerificationProperties;
import kr.co.pawpaw.api.dto.sms.CheckVerificationCodeRequest;
import kr.co.pawpaw.api.dto.sms.CheckVerificationCodeResponse;
import kr.co.pawpaw.api.dto.sms.SendVerificationCodeRequest;
import kr.co.pawpaw.common.exception.sms.InvalidVerificationCodeException;
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
import java.util.Optional;

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
        final SendVerificationCodeRequest request,
        final SmsUsagePurpose usagePurpose
    ) {
        Recipient recipient = request.getRecipient();

        checkSmsLimitPerDay(recipient, usagePurpose);
        recipient.deleteToHyphen();
        VerificationCode code = getVerificationCode(request, usagePurpose);
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
        VerificationCode vCode = getVerificationCode(request, usagePurpose)
            .orElseThrow(InvalidVerificationCodeException::new);

        saveVerifiedPhoneNumber(vCode);
        verificationCodeCommand.deleteById(vCode.getId());

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

    private Optional<VerificationCode> getVerificationCode(
        final CheckVerificationCodeRequest request,
        final SmsUsagePurpose usagePurpose
    ) {
        return verificationCodeQuery.findByPhoneNumberAndUsagePurposeAndCode(
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

    private void saveVerifiedPhoneNumber(
        final VerificationCode vCode
    ) {
        VerifiedPhoneNumber verifiedPhoneNumber = VerifiedPhoneNumber.builder()
            .phoneNumber(vCode.getPhoneNumber())
            .userName(vCode.getName())
            .usagePurpose(vCode.getUsagePurpose())
            .build();
        verifiedPhoneNumberCommand.save(verifiedPhoneNumber);
    }

    private VerificationCode getVerificationCode(
        final SendVerificationCodeRequest request,
        final SmsUsagePurpose usagePurpose
    ) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(verificationProperties.getCodeLength());

        for (int i = 0; i < verificationProperties.getCodeLength(); ++i) {
            sb.append(Math.abs(random.nextInt()) % 10);
        }

        return VerificationCode.builder()
            .phoneNumber(request.getRecipient().getTo())
            .name(request.getName())
            .usagePurpose(usagePurpose.name())
            .code(sb.toString())
            .build();
    }

    private String getVerificationContent(final String code) {
        return "[PawPaw] 인증번호[" + code + "]를 입력해주세요.";
    }
}
