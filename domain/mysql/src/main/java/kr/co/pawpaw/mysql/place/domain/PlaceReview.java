package kr.co.pawpaw.mysql.place.domain;

import kr.co.pawpaw.mysql.common.BaseTimeEntity;
import kr.co.pawpaw.mysql.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceReview extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    private User reviewer;

    private Long score;
    private boolean isScenic;
    private boolean isQuiet;
    private boolean isClean;
    private boolean isComfortable;
    private boolean isSafe;
    private boolean isAccessible;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "placeReview", cascade = CascadeType.REMOVE)
    private final List<PlaceReviewImage> placeReviewImageList = new ArrayList<>();

    @Builder
    public PlaceReview(
        final Place place,
        final User reviewer,
        final Long score,
        final boolean isScenic,
        final boolean isQuiet,
        final boolean isClean,
        final boolean isComfortable,
        final boolean isSafe,
        final boolean isAccessible,
        final String content
    ) {
        this.place = place;
        this.reviewer = reviewer;
        this.score = score;
        this.isScenic = isScenic;
        this.isQuiet = isQuiet;
        this.isClean = isClean;
        this.isComfortable = isComfortable;
        this.isSafe = isSafe;
        this.isAccessible = isAccessible;
        this.content = content;
    }

    public void addReviewImageList(final Collection<PlaceReviewImage> placeReviewImageList) {
        this.placeReviewImageList.addAll(placeReviewImageList);
        this.placeReviewImageList.forEach(placeReviewImage -> placeReviewImage.updatePlaceReview(this));
    }

    public void updateReview(
        final Long score,
        final boolean isScenic,
        final boolean isQuiet,
        final boolean isClean,
        final boolean isComfortable,
        final boolean isSafe,
        final boolean isAccessible,
        final String content
    ) {
        this.score = score;
        this.isScenic = isScenic;
        this.isQuiet = isQuiet;
        this.isClean = isClean;
        this.isComfortable = isComfortable;
        this.isSafe = isSafe;
        this.isAccessible = isAccessible;
        this.content = content;
    }
}
