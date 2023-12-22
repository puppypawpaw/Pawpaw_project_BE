package kr.co.pawpaw.mysql.place.service.query;

import kr.co.pawpaw.mysql.place.domain.Place;
import kr.co.pawpaw.mysql.place.dto.PlaceTopBookmarkPercentageResponse;
import kr.co.pawpaw.mysql.place.enums.PlaceType;
import kr.co.pawpaw.mysql.place.dto.PlaceQueryDSLResponse;
import kr.co.pawpaw.mysql.place.repository.PlaceCustomRepository;
import kr.co.pawpaw.mysql.place.repository.PlaceRepository;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceQuery {
    private final PlaceCustomRepository placeCustomRepository;
    private final PlaceRepository placeRepository;

    public List<PlaceQueryDSLResponse> findByQueryAndPlaceTypeAndPositionRange(
        final String query,
        final PlaceType placeType,
        final Double latitudeMin,
        final Double latitudeMax,
        final Double longitudeMin,
        final Double longitudeMax,
        final UserId userId
    ) {
        return placeCustomRepository.findByQueryAndPlaceTypeAndPositionRange(
            query,
            placeType,
            latMinNullCheck(latitudeMin),
            latMaxNullCheck(latitudeMax),
            longMinNullCheck(longitudeMin),
            longMaxNullCheck(longitudeMax),
            userId
        );
    }

    public Collection<PlaceTopBookmarkPercentageResponse> findPlaceTopBookmarkPercentageList(final Collection<Long> placeIds) {
        return placeCustomRepository.findPlaceTopBookmarkPercentageList(placeIds);
    }

    public Optional<PlaceQueryDSLResponse> findByPlaceIdAsPlaceResponse(final Long placeId) {
        return placeCustomRepository.findById(placeId);
    }

    public Optional<Place> findByPlaceId(final Long placeId) {
        return placeRepository.findById(placeId);
    }

    public boolean existsById(final Long placeId) {
        return placeRepository.existsById(placeId);
    }

    private double latMinNullCheck(final Double min) {
        return Objects.nonNull(min) ? min : -90;
    }

    private double latMaxNullCheck(final Double max) {
        return Objects.nonNull(max) ? max : 90;
    }

    private double longMinNullCheck(final Double min) {
        return Objects.nonNull(min) ? min : -180;
    }

    private double longMaxNullCheck(final Double max) {
        return Objects.nonNull(max) ? max : 180;
    }
}
