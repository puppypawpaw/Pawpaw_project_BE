package kr.co.pawpaw.mysql.place.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PlaceReviewImageResponse {
    @Schema(description = "장소 리뷰 이미지 ID", example = "1")
    private Long id;

    @Schema(description = "장소 리뷰 이미지 URL", example = "https://example.com")
    private String imageUrl;

    @QueryProjection
    public PlaceReviewImageResponse(final Long id, final String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }
}
