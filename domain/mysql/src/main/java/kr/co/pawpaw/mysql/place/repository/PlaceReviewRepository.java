package kr.co.pawpaw.mysql.place.repository;

import kr.co.pawpaw.mysql.place.domain.PlaceReview;
import kr.co.pawpaw.mysql.user.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceReviewRepository extends JpaRepository<PlaceReview, Long> {
    boolean existsByPlaceIdAndReviewerUserId(final Long id, final UserId userId);

    void deleteByPlaceIdAndReviewerUserId(final Long placeId, final UserId userId);
}
