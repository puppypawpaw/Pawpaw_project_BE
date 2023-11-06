package kr.co.pawpaw.mysql.place.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import kr.co.pawpaw.mysql.common.dto.PositionResponse;
import kr.co.pawpaw.mysql.common.util.QueryUtil;
import kr.co.pawpaw.mysql.place.domain.Place;
import kr.co.pawpaw.mysql.place.domain.PlaceReview;
import kr.co.pawpaw.mysql.place.domain.PlaceType;
import kr.co.pawpaw.mysql.place.domain.QPlace;
import kr.co.pawpaw.mysql.place.dto.PlaceResponse;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PlaceCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public List<PlaceResponse> findByQueryAndPlaceTypeAndPositionRange(
        final String query,
        final PlaceType placeType,
        final double latitudeMin,
        final double latitudeMax,
        final double longitudeMin,
        final double longitudeMax,
        final UserId userId
    ) {
        BooleanBuilder condition = new BooleanBuilder();

        if (Objects.nonNull(query)) {
            condition = condition.and(QueryUtil.fullTextSearchCondition(QPlace.place.name, query));
        }

        if (Objects.nonNull(placeType)) {
            condition = condition.and(QPlace.place.placeType.eq(placeType));
        }

        condition = condition
            .and(QueryUtil.withInPositionCondition(QPlace.place.position.latitude, QPlace.place.position.longitude, latitudeMin, latitudeMax, longitudeMin, longitudeMax));

        return queryFactory
            .selectFrom(QPlace.place)
            .innerJoin(QPlace.place.placeImageUrls)
            .where(condition)
            .distinct()
            .fetch()
            .stream()
            .map(place -> new PlaceResponse(
                place.getId(),
                place.getPlaceImageUrls(),
                place.getName(),
                new PositionResponse(
                    place.getPosition().getLatitude(),
                    place.getPosition().getLongitude(),
                    place.getPosition().getAddress()
                ),
                place.getOpenHours(),
                false, // TODO userId 기반 북마크 여부 확인
                getRatio(place.getReviewInfo().getTotalScore(), place.getReviewInfo().getReviewCnt()),
                getRatio(place.getReviewInfo().getScenicCnt(), place.getReviewInfo().getReviewCnt()),
                getRatio(place.getReviewInfo().getQuietCnt(), place.getReviewInfo().getReviewCnt()),
                getRatio(place.getReviewInfo().getComfortableCnt(), place.getReviewInfo().getReviewCnt()),
                getRatio(place.getReviewInfo().getAccessibleCnt(), place.getReviewInfo().getReviewCnt()),
                getRatio(place.getReviewInfo().getCleanCnt(), place.getReviewInfo().getReviewCnt()),
                getRatio(place.getReviewInfo().getSafeCnt(), place.getReviewInfo().getReviewCnt())
            )).collect(Collectors.toList());
    }

    public void updatePlaceReviewInfo(
        final Place place,
        final PlaceReview placeReview
    ) {
        JPAUpdateClause updateClause = queryFactory.update(QPlace.place)
            .where(QPlace.place.id.eq(place.getId()))
            .set(QPlace.place.reviewInfo.reviewCnt, QPlace.place.reviewInfo.reviewCnt.add(1))
            .set(QPlace.place.reviewInfo.totalScore, QPlace.place.reviewInfo.totalScore.add(placeReview.getScore()));

        if (placeReview.isQuiet()) updateClause.set(QPlace.place.reviewInfo.quietCnt, QPlace.place.reviewInfo.quietCnt.add(1));
        if (placeReview.isAccessible()) updateClause.set(QPlace.place.reviewInfo.accessibleCnt, QPlace.place.reviewInfo.accessibleCnt.add(1));
        if (placeReview.isSafe()) updateClause.set(QPlace.place.reviewInfo.safeCnt, QPlace.place.reviewInfo.safeCnt.add(1));
        if (placeReview.isScenic()) updateClause.set(QPlace.place.reviewInfo.scenicCnt, QPlace.place.reviewInfo.scenicCnt.add(1));
        if (placeReview.isClean()) updateClause.set(QPlace.place.reviewInfo.cleanCnt, QPlace.place.reviewInfo.cleanCnt.add(1));
        if (placeReview.isComfortable()) updateClause.set(QPlace.place.reviewInfo.comfortableCnt, QPlace.place.reviewInfo.comfortableCnt.add(1));

        updateClause.execute();
        entityManager.refresh(entityManager.find(Place.class, place.getId()));
    }

    private Double getRatio(final double son, final double mom) {
        if (mom == 0) {
            return null;
        }

        return son / mom;
    }
}
