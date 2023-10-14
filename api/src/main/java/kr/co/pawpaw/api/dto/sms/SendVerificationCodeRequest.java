package kr.co.pawpaw.api.dto.sms;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.feignClient.dto.Recipient;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SendVerificationCodeRequest {
    @Valid
    @NotNull
    @Schema(description = "수신자", example = "01012345678")
    private Recipient recipient;
    @NotBlank
    @Schema(description = "이름", example = "김민수")
    private String name;
    @NotBlank
    @Schema(description = "생년월일", example = "20000503")
    private String birthday;
}
