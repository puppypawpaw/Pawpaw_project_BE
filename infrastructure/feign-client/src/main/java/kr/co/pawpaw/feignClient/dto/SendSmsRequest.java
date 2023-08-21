package kr.co.pawpaw.feignClient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SendSmsRequest {
    private String type;
    private String from;
    private String content;
    private List<Recipient> messages;
}
