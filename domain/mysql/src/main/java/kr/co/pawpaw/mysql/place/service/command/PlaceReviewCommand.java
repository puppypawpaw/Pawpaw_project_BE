package kr.co.pawpaw.mysql.place.service.command;

import kr.co.pawpaw.mysql.place.domain.PlaceReview;
import kr.co.pawpaw.mysql.place.repository.PlaceReviewRepository;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceReviewCommand {
    private final PlaceReviewRepository placeReviewRepository;

    public PlaceReview save(final PlaceReview placeReview) {
        return placeReviewRepository.save(placeReview);
    }

    public void deleteByPlaceIdAndReviewerUserId(final Long placeId, final UserId userId) {
        placeReviewRepository.deleteByPlaceIdAndReviewerUserId(placeId, userId);
    }
}
