package kr.co.pawpaw.api.dto.position;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.mysql.common.domain.Position;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PositionRequest {
    @NotNull
    @Schema(description = "위도", example = "36.5")
    private Double latitude;
    @NotNull
    @Schema(description = "경도", example = "36.4")
    private Double longitude;
    @NotBlank
    @Schema(description = "주소", example = "서울특별시 강동구 고덕동")
    private String address;

    public Position toEntity() {
        return Position.builder()
            .latitude(latitude)
            .longitude(longitude)
            .address(address)
            .build();
    }
}
