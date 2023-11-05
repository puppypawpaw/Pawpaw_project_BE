package kr.co.pawpaw.mysql.place.repository;

import kr.co.pawpaw.mysql.place.domain.PlaceReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceReviewRepository extends JpaRepository<PlaceReview, Long> {
}
