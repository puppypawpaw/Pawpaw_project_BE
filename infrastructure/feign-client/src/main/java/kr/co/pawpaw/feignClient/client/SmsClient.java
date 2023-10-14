package kr.co.pawpaw.feignClient.client;

import kr.co.pawpaw.feignClient.config.NaverCloudSmsConfig;
import kr.co.pawpaw.feignClient.dto.SendSmsResponse;
import kr.co.pawpaw.feignClient.dto.SendSmsRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(configuration = NaverCloudSmsConfig.class, name = "smsClient", url = "https://sens.apigw.ntruss.com")
public interface SmsClient {
    @PostMapping("/sms/v2/services/{serviceId}/messages")
    SendSmsResponse sendMessage(
        @PathVariable("serviceId") final String serviceId,
        @RequestBody final SendSmsRequest request
    );
}
