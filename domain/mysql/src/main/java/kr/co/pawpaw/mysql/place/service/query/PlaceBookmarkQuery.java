package kr.co.pawpaw.mysql.place.service.query;

import kr.co.pawpaw.mysql.place.repository.PlaceBookmarkRepository;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceBookmarkQuery {
    private final PlaceBookmarkRepository placeBookmarkRepository;

    public boolean existsByPlaceIdAndUserId(final Long placeId, final UserId userId) {
        return placeBookmarkRepository.existsByPlaceIdAndUserUserId(placeId, userId);
    }
}
