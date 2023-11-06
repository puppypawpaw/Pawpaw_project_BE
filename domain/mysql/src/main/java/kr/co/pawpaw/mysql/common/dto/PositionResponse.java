package kr.co.pawpaw.mysql.common.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.mysql.common.domain.Position;
import lombok.Getter;

@Getter
public class PositionResponse {
    @Schema(description = "위도", example = "13.8")
    private Double latitude;
    @Schema(description = "경도", example = "24.3")
    private Double longitude;
    @Schema(description = "주소", example = "서울특별시")
    private String address;

    @QueryProjection
    public PositionResponse(
        final Double latitude,
        final Double longitude,
        final String address
    ) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public static PositionResponse of(final Position position) {
        return new PositionResponse(
            position.getLatitude(),
            position.getLongitude(),
            position.getAddress()
        );
    }
}
