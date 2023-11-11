package kr.co.pawpaw.mysql.place.repository;

import kr.co.pawpaw.mysql.place.domain.PlaceReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PlaceReviewImageRepository extends JpaRepository<PlaceReviewImage, Long> {
    void deleteByPlaceReviewIdAndIdIn(final Long placeReviewId, final Collection<Long> placeReviewImageIdList);
}
