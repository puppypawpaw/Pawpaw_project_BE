package kr.co.pawpaw.api.dto.place;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.mysql.place.domain.Place;
import kr.co.pawpaw.mysql.place.domain.PlaceReview;
import kr.co.pawpaw.mysql.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreatePlaceReviewRequest {
    @Schema(description = "평점(5점만점)", example = "4")
    private long score;
    @Schema(description = "조경이 좋아요", type = "BOOLEAN", example = "true")
    private boolean scenic;
    @Schema(description = "조용해요", type = "BOOLEAN", example = "true")
    private boolean quiet;
    @Schema(description = "깨끗해요", type = "BOOLEAN", example = "true")
    private boolean clean;
    @Schema(description = "쾌적해요", type = "BOOLEAN", example = "true")
    private boolean comfortable;
    @Schema(description = "안전해요", type = "BOOLEAN", example = "true")
    private boolean safe;
    @Schema(description = "접근성이 좋아요", type = "BOOLEAN", example = "true")
    private boolean accessible;
    @Schema(description = "리뷰 내용", type = "STRING", example = "그래프가 제 취향과 너무 잘 맞아서 방문했습니다. 쾌적 그래프가 높던데 역시나 넘나 쾌적하고 뭉이랑 감자도 신나게 놀았어요")
    private String content;

    public PlaceReview toPlaceReview(
        final Place place,
        final User reviewer
    ) {
        return PlaceReview.builder()
            .place(place)
            .reviewer(reviewer)
            .score(score)
            .isScenic(scenic)
            .isQuiet(quiet)
            .isClean(clean)
            .isComfortable(comfortable)
            .isSafe(safe)
            .isAccessible(accessible)
            .content(content)
            .build();
    }
}
