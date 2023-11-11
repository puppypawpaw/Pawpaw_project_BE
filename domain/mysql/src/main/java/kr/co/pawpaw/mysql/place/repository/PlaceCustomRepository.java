package kr.co.pawpaw.mysql.place.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import kr.co.pawpaw.mysql.common.dto.QPositionResponse;
import kr.co.pawpaw.mysql.common.util.QueryUtil;
import kr.co.pawpaw.mysql.place.domain.*;
import kr.co.pawpaw.mysql.place.dto.PlaceResponse;
import kr.co.pawpaw.mysql.place.dto.QPlaceResponse;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;

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
            .from(QPlace.place)
            .innerJoin(QPlaceImageUrl.placeImageUrl).on(QPlaceImageUrl.placeImageUrl.place.eq(QPlace.place))
            .leftJoin(QPlaceBookmark.placeBookmark).on(QPlaceBookmark.placeBookmark.place.eq(QPlace.place))
            .where(condition)
            .transform(groupBy(QPlace.place.id)
                .list(new QPlaceResponse(
                    QPlace.place.id,
                    set(QPlaceImageUrl.placeImageUrl.url),
                    QPlace.place.name,
                    new QPositionResponse(
                        QPlace.place.position.latitude,
                        QPlace.place.position.longitude,
                        QPlace.place.position.address
                    ),
                    QPlace.place.openHours,
                    QPlaceBookmark.placeBookmark.id.isNotNull(),
                    QPlace.place.reviewInfo.totalScore.doubleValue().divide(QPlace.place.reviewInfo.reviewCnt),
                    QPlace.place.reviewInfo.scenicCnt.doubleValue().divide(QPlace.place.reviewInfo.reviewCnt),
                    QPlace.place.reviewInfo.quietCnt.doubleValue().divide(QPlace.place.reviewInfo.reviewCnt),
                    QPlace.place.reviewInfo.comfortableCnt.doubleValue().divide(QPlace.place.reviewInfo.reviewCnt),
                    QPlace.place.reviewInfo.accessibleCnt.doubleValue().divide(QPlace.place.reviewInfo.reviewCnt),
                    QPlace.place.reviewInfo.cleanCnt.doubleValue().divide(QPlace.place.reviewInfo.reviewCnt),
                    QPlace.place.reviewInfo.safeCnt.doubleValue().divide(QPlace.place.reviewInfo.reviewCnt)
                )));
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
        JPAUpdateClause updateClause = queryFactory.update(QPlace.place)
            .where(QPlace.place.id.eq(placeId))
            .set(QPlace.place.reviewInfo.reviewCnt, QPlace.place.reviewInfo.reviewCnt.add(cntAdd))
            .set(QPlace.place.reviewInfo.totalScore, QPlace.place.reviewInfo.totalScore.add(scoreAdd));

        if (quietAdd != 0) updateClause.set(QPlace.place.reviewInfo.quietCnt, QPlace.place.reviewInfo.quietCnt.add(quietAdd));
        if (accessibleAdd != 0) updateClause.set(QPlace.place.reviewInfo.accessibleCnt, QPlace.place.reviewInfo.accessibleCnt.add(accessibleAdd));
        if (safeAdd != 0) updateClause.set(QPlace.place.reviewInfo.safeCnt, QPlace.place.reviewInfo.safeCnt.add(safeAdd));
        if (scenicAdd != 0) updateClause.set(QPlace.place.reviewInfo.scenicCnt, QPlace.place.reviewInfo.scenicCnt.add(scenicAdd));
        if (cleanAdd != 0) updateClause.set(QPlace.place.reviewInfo.cleanCnt, QPlace.place.reviewInfo.cleanCnt.add(cleanAdd));
        if (comfortableAdd != 0) updateClause.set(QPlace.place.reviewInfo.comfortableCnt, QPlace.place.reviewInfo.comfortableCnt.add(comfortableAdd));

        updateClause.execute();
        entityManager.refresh(entityManager.find(Place.class, placeId));
    }

    private Double getRatio(final double son, final double mom) {
        if (mom == 0) {
            return null;
        }

        return son / mom;
    }
}
