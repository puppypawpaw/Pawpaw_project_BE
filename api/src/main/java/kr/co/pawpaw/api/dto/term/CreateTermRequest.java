package kr.co.pawpaw.api.dto.term;


import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.domainrdb.term.domain.Term;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateTermRequest {
    @NotBlank
    @Schema(description = "약관 제목")
    private String title;
    @NotBlank
    @Schema(description = "약관 내용")
    private String content;
    @NotNull
    @Schema(description = "필수 여부")
    private Boolean required;
    @Schema(description = "약관 순서(null 가능)")
    private Long order;

    public Term toEntity() {
        return Term.builder()
            .title(title)
            .content(content)
            .required(required)
            .order(order)
            .build();
    }
}
