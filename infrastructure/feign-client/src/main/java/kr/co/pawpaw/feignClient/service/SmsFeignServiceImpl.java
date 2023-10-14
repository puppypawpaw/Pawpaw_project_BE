package kr.co.pawpaw.feignClient.service;

import kr.co.pawpaw.feignClient.client.SmsClient;
import kr.co.pawpaw.feignClient.dto.Recipient;
import kr.co.pawpaw.feignClient.dto.SendSmsRequest;
import kr.co.pawpaw.feignClient.dto.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Profile("prod|dev")
@Service
public class SmsFeignServiceImpl implements SmsFeignService {
    private final SmsClient smsClient;
    private final String from;
    private final String serviceId;

    public SmsFeignServiceImpl(
        final SmsClient smsClient,
        @Value("${feign.sms.naver.from}") final String from,
        @Value("${feign.sms.naver.service-id}") final String serviceId
    ) {
        this.from = from;
        this.smsClient = smsClient;
        this.serviceId = serviceId;
    }

    public SendSmsResponse sendSmsMessage(
        final String content,
        final Recipient recipient
    ) {
        log.info("\n[SMS] - " + recipient.getTo() + "\n" + content);

        return smsClient.sendMessage(
            serviceId,
            SendSmsRequest.builder()
                .type("SMS")
                .from(from)
                .messages(List.of(recipient))
                .content(content)
                .build()
        );
    }
}
