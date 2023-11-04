package kr.co.pawpaw.api.dto.position;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.mysql.common.domain.Position;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PositionResponse {
    @Schema(description = "위도", example = "37.5397267")
    private Double latitude;
    @Schema(description = "경도", example = "127.1281564")
    private Double longitude;
    @Schema(description = "주소", example = "서울특별시 강동구 천호동 410-1 1층 곱창팩토리")
    private String address;

    public static PositionResponse of(final Position position) {
        return new PositionResponse(
            position.getLatitude(),
            position.getLongitude(),
            position.getAddress()
        );
    }
}
