package kr.co.pawpaw.mysql.place.service.command;

import kr.co.pawpaw.mysql.place.domain.PlaceBookmark;
import kr.co.pawpaw.mysql.place.repository.PlaceBookmarkRepository;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceBookmarkCommand {
    private final PlaceBookmarkRepository placeBookmarkRepository;

    public PlaceBookmark save(final PlaceBookmark placeBookmark) {
        return placeBookmarkRepository.save(placeBookmark);
    }

    public void deleteByPlaceIdAndUserUserId(final Long placeId, final UserId userId) {
        placeBookmarkRepository.deleteByPlaceIdAndUserUserId(placeId, userId);
    }
}
