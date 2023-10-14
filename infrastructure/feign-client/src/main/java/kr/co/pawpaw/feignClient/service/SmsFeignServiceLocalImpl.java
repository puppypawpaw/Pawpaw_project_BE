package kr.co.pawpaw.feignClient.service;

import kr.co.pawpaw.feignClient.dto.Recipient;
import kr.co.pawpaw.feignClient.dto.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Profile("local")
@Service
public class SmsFeignServiceLocalImpl implements SmsFeignService {
    public SendSmsResponse sendSmsMessage(
        final String content,
        final Recipient recipient
    ) {
        log.info("\n[SMS] - " + recipient.getTo() + "\n" + content);
        return new SendSmsResponse();
    }
}
