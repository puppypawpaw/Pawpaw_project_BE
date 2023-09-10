package kr.co.pawpaw.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SendEmailRequest {
    private String to;
    private String subject;
    private String text;
}
