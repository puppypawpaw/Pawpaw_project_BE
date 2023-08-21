package kr.co.pawpaw.feignClient.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recipient {
    private String to;

    public void deleteToHyphen() {
        to = to.replaceAll("-", "");
    }
}