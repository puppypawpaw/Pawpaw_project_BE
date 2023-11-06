package kr.co.pawpaw.mysql.place.domain;

import kr.co.pawpaw.mysql.common.BaseTimeEntity;
import kr.co.pawpaw.mysql.storage.domain.File;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceReviewImage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private PlaceReview placeReview;

    @ManyToOne(fetch = FetchType.LAZY)
    private File image;

    @Builder
    public PlaceReviewImage(
        final PlaceReview placeReview,
        final File image
    ) {
        this.placeReview = placeReview;
        this.image = image;
    }

    public void updatePlaceReview(final PlaceReview placeReview) {
        this.placeReview = placeReview;
    }
}
