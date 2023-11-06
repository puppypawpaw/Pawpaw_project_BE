package kr.co.pawpaw.mysql.place.repository;

import kr.co.pawpaw.mysql.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
