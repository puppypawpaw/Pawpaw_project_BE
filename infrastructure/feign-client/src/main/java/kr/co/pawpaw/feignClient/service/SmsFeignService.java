package kr.co.pawpaw.feignClient.service;

import kr.co.pawpaw.feignClient.dto.Recipient;
import kr.co.pawpaw.feignClient.dto.SendSmsResponse;

public interface SmsFeignService {
    SendSmsResponse sendSmsMessage(final String content, final Recipient recipient);
}
