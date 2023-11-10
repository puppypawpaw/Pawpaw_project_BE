package kr.co.pawpaw.mysql.place.service.command;

import kr.co.pawpaw.mysql.place.domain.Place;
import kr.co.pawpaw.mysql.place.repository.PlaceCustomRepository;
import kr.co.pawpaw.mysql.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceCommand {
    private final PlaceRepository placeRepository;
    private final PlaceCustomRepository placeCustomRepository;

    public List<Place> saveAll(final List<Place> placeList) {
        return placeRepository.saveAll(placeList);
    }

    public void updatePlaceReviewInfo(
        final Long placeId,
        final long cntAdd,
        final long scoreAdd,
        final long quietAdd,
        final long accessibleAdd,
        final long safeAdd,
        final long scenicAdd,
        final long cleanAdd,
        final long comfortableAdd
    ) {
        placeCustomRepository.updatePlaceReviewInfo(
            placeId,
            cntAdd,
            scoreAdd,
            quietAdd,
            accessibleAdd,
            safeAdd,
            scenicAdd,
            cleanAdd,
            comfortableAdd
        );
    }
}
