package kr.co.pawpaw.feignClient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SendSmsResponse {
    private String requestId;
    private String requestTime;
    private String statusCode;
    private String statusName;
}
