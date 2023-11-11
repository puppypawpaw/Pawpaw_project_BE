package kr.co.pawpaw.mysql.place.repository;

import kr.co.pawpaw.mysql.place.domain.PlaceBookmark;
import kr.co.pawpaw.mysql.user.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceBookmarkRepository extends JpaRepository<PlaceBookmark, Long> {
    void deleteByPlaceIdAndUserUserId(final Long placeId, final UserId userId);
    boolean existsByPlaceIdAndUserUserId(final Long placeId, final UserId userId);
}
