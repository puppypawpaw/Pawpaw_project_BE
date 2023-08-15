package kr.co.pawpaw.domainrdb.term.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.domainrdb.term.domain.Term;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
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
