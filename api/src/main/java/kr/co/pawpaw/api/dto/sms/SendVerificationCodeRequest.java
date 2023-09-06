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
    @Schema(description = "수신자")
    private Recipient recipient;
    @NotBlank
    @Schema(description = "이름")
    private String name;
    @NotBlank
    @Schema(description = "생년월일")
    private String birthday;
}
