package kr.co.pawpaw.api.dto.position;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.domainrdb.position.Position;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PositionResponse {
    @Schema(description = "위도")
    private double latitude;
    @Schema(description = "경도")
    private double longitude;
    @NotBlank
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
