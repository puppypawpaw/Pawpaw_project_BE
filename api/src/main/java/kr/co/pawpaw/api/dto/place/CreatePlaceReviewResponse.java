package kr.co.pawpaw.api.dto.place;

import kr.co.pawpaw.mysql.place.domain.PlaceReview;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatePlaceReviewResponse {
    private Long placeReviewId;

    public static CreatePlaceReviewResponse of(final PlaceReview placeReview) {
        return new CreatePlaceReviewResponse(placeReview.getId());
    }
}
