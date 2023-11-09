package kr.co.pawpaw.mysql.place.service.command;

import kr.co.pawpaw.mysql.place.domain.PlaceReviewImage;
import kr.co.pawpaw.mysql.place.repository.PlaceReviewImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class PlaceReviewImageCommand {
    private final PlaceReviewImageRepository placeReviewImageRepository;

    public Collection<PlaceReviewImage> saveAll(final Collection<PlaceReviewImage> placeReviewImageCollection) {
        return placeReviewImageRepository.saveAll(placeReviewImageCollection);
    }
}
