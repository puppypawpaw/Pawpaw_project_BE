package kr.co.pawpaw.api.dto.place;

import kr.co.pawpaw.api.dto.position.PositionRequest;
import kr.co.pawpaw.mysql.place.domain.Place;
import kr.co.pawpaw.mysql.place.domain.PlaceType;
import lombok.*;

import java.util.Collection;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatePlaceRequest {
    private String name;
    private Collection<String> placeImageUrls;
    private PlaceType placeType;
    private PositionRequest positionRequest;
    private String openHours;

    public Place toPlace() {
        return Place.builder()
            .name(name)
            .placeImageUrls(placeImageUrls)
            .placeType(placeType)
            .position(positionRequest.toEntity())
            .openHours(openHours)
            .build();
    }
}
