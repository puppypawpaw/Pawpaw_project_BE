package kr.co.pawpaw.mysql.place.dto;

import lombok.Getter;

@Getter
public class PlaceTopBookmarkPercentageResponse {
    private final Long placeId;
    private final Double topBookmarkPercentage;

    public PlaceTopBookmarkPercentageResponse(
        Long placeId,
        Double topBookmarkPercentage
    ) {
        this.placeId = placeId;
        this.topBookmarkPercentage = topBookmarkPercentage;
    }
}
