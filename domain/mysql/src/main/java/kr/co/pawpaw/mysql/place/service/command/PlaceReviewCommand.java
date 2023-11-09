package kr.co.pawpaw.mysql.place.service.command;

import kr.co.pawpaw.mysql.place.domain.PlaceReview;
import kr.co.pawpaw.mysql.place.repository.PlaceReviewRepository;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceReviewCommand {
    private final PlaceReviewRepository placeReviewRepository;

    public PlaceReview save(final PlaceReview placeReview) {
        return placeReviewRepository.save(placeReview);
    }

    public void delete(final PlaceReview placeReview) {
        placeReviewRepository.delete(placeReview);
    }

    public Optional<PlaceReview> findByPlaceIdAndReviewerUserId(final Long placeId, final UserId userId) {
        return placeReviewRepository.findByPlaceIdAndReviewerUserId(placeId, userId);
    }
}
