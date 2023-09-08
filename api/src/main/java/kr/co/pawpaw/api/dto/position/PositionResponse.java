package kr.co.pawpaw.api.dto.position;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.domainrdb.position.Position;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PositionResponse {
    @Schema(description = "위도")
    private Double latitude;
    @Schema(description = "경도")
    private Double longitude;
    @Schema(description = "장소 이름")
    private String name;

    public static PositionResponse of(final Position position) {
        return new PositionResponse(
            position.getLatitude(),
            position.getLongitude(),
            position.getName()
        );
    }
}
