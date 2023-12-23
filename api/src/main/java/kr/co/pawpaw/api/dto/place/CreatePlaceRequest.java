package kr.co.pawpaw.api.dto.place;

import kr.co.pawpaw.api.dto.position.PositionRequest;
import kr.co.pawpaw.mysql.place.domain.Place;
import kr.co.pawpaw.mysql.place.domain.PlaceImageUrl;
import kr.co.pawpaw.mysql.place.enums.PlaceType;
import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatePlaceRequest {
    private String name;
    private Collection<String> placeImageUrls;
    private PlaceType placeType;
    private PositionRequest positionRequest;

    public Place toPlace() {
        return Place.builder()
            .name(name)
            .placeType(placeType)
            .position(positionRequest.toEntity())
            .build();
    }

    public List<PlaceImageUrl> toPlaceImageUrls(final Place place) {
        return this.placeImageUrls.stream()
            .map(url -> PlaceImageUrl.builder()
                .url(url)
                .place(place)
                .build())
            .collect(Collectors.toList());
    }
}
