package kr.co.pawpaw.mysql.place.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import kr.co.pawpaw.mysql.common.dto.QPositionResponse;
import kr.co.pawpaw.mysql.common.repository.OrderByNull;
import kr.co.pawpaw.mysql.common.util.QueryUtil;
import kr.co.pawpaw.mysql.place.domain.Place;
import kr.co.pawpaw.mysql.place.domain.QPlace;
import kr.co.pawpaw.mysql.place.domain.QPlaceBookmark;
import kr.co.pawpaw.mysql.place.domain.QPlaceImageUrl;
import kr.co.pawpaw.mysql.place.dto.PlaceQueryDSLResponse;
import kr.co.pawpaw.mysql.place.dto.PlaceTopBookmarkPercentageResponse;
import kr.co.pawpaw.mysql.place.dto.QPlaceQueryDSLResponse;
import kr.co.pawpaw.mysql.place.enums.PlaceType;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;
import static com.querydsl.core.types.ExpressionUtils.count;
import static com.querydsl.core.types.dsl.Wildcard.all;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public Optional<PlaceQueryDSLResponse> findById(final Long placeId) {
        return queryFactory.from(QPlace.place)
            .innerJoin(QPlaceImageUrl.placeImageUrl)
            .on(QPlaceImageUrl.placeImageUrl.place.eq(QPlace.place))
            .leftJoin(QPlaceBookmark.placeBookmark)
            .on(QPlaceBookmark.placeBookmark.place.eq(QPlace.place))
            .where(QPlace.place.id.eq(placeId))
            .transform(groupBy(QPlace.place.id)
                .list(new QPlaceQueryDSLResponse(
                    QPlace.place.id,
                    QPlace.place.placeType,
                    set(QPlaceImageUrl.placeImageUrl.url),
                    QPlace.place.name,
                    new QPositionResponse(
                        QPlace.place.position.latitude,
                        QPlace.place.position.longitude,
                        QPlace.place.position.address
                    ),
                    QPlace.place.monOpenHour.open,
                    QPlace.place.monOpenHour.close,
                    QPlace.place.monOpenHour.lastOrder,
                    QPlace.place.tueOpenHour.open,
                    QPlace.place.tueOpenHour.close,
                    QPlace.place.tueOpenHour.lastOrder,
                    QPlace.place.wedOpenHour.open,
                    QPlace.place.wedOpenHour.close,
                    QPlace.place.wedOpenHour.lastOrder,
                    QPlace.place.thuOpenHour.open,
                    QPlace.place.thuOpenHour.close,
                    QPlace.place.thuOpenHour.lastOrder,
                    QPlace.place.friOpenHour.open,
                    QPlace.place.friOpenHour.close,
                    QPlace.place.friOpenHour.lastOrder,
                    QPlace.place.satOpenHour.open,
                    QPlace.place.satOpenHour.close,
                    QPlace.place.satOpenHour.lastOrder,
                    QPlace.place.sunOpenHour.open,
                    QPlace.place.sunOpenHour.close,
                    QPlace.place.sunOpenHour.lastOrder,
                    QPlaceBookmark.placeBookmark.id.isNotNull(),
                    QPlace.place.reviewInfo.totalScore.doubleValue().divide(QPlace.place.reviewInfo.reviewCnt),
                    QPlace.place.reviewInfo.scenicCnt.doubleValue().divide(QPlace.place.reviewInfo.reviewCnt),
                    QPlace.place.reviewInfo.quietCnt.doubleValue().divide(QPlace.place.reviewInfo.reviewCnt),
                    QPlace.place.reviewInfo.comfortableCnt.doubleValue().divide(QPlace.place.reviewInfo.reviewCnt),
                    QPlace.place.reviewInfo.accessibleCnt.doubleValue().divide(QPlace.place.reviewInfo.reviewCnt),
                    QPlace.place.reviewInfo.cleanCnt.doubleValue().divide(QPlace.place.reviewInfo.reviewCnt),
                    QPlace.place.reviewInfo.safeCnt.doubleValue().divide(QPlace.place.reviewInfo.reviewCnt)
                    )
                )
            ).stream()
            .findAny();
    }

    public List<PlaceQueryDSLResponse> findByQueryAndPlaceTypeAndPositionRange(final String query, final PlaceType placeType, final double latitudeMin, final double latitudeMax, final double longitudeMin, final double longitudeMax, final UserId userId) {
        BooleanBuilder condition = new BooleanBuilder();

        if (Objects.nonNull(query)) {
            condition = condition.and(QueryUtil.fullTextSearchCondition(QPlace.place.name, query));
        }

        if (Objects.nonNull(placeType)) {
            condition = condition.and(QPlace.place.placeType.eq(placeType));
        }

        condition = condition.and(QueryUtil.withInPositionCondition(QPlace.place.position.latitude, QPlace.place.position.longitude, latitudeMin, latitudeMax, longitudeMin, longitudeMax));

        return queryFactory
            .from(QPlace.place)
            .innerJoin(QPlaceImageUrl.placeImageUrl)
            .on(QPlaceImageUrl.placeImageUrl.place.eq(QPlace.place))
            .leftJoin(QPlaceBookmark.placeBookmark)
            .on(QPlaceBookmark.placeBookmark.place.eq(QPlace.place))
            .where(condition)
            .transform(groupBy(QPlace.place.id)
                .list(new QPlaceQueryDSLResponse(
                    QPlace.place.id,
                    QPlace.place.placeType,
                    set(QPlaceImageUrl.placeImageUrl.url),
                    QPlace.place.name,
                    new QPositionResponse(
                        QPlace.place.position.latitude,
                        QPlace.place.position.longitude,
                        QPlace.place.position.address
                    ),
                    QPlace.place.monOpenHour.open,
                    QPlace.place.monOpenHour.close,
                    QPlace.place.monOpenHour.lastOrder,
                    QPlace.place.tueOpenHour.open,
                    QPlace.place.tueOpenHour.close,
                    QPlace.place.tueOpenHour.lastOrder,
                    QPlace.place.wedOpenHour.open,
                    QPlace.place.wedOpenHour.close,
                    QPlace.place.wedOpenHour.lastOrder,
                    QPlace.place.thuOpenHour.open,
                    QPlace.place.thuOpenHour.close,
                    QPlace.place.thuOpenHour.lastOrder,
                    QPlace.place.friOpenHour.open,
                    QPlace.place.friOpenHour.close,
                    QPlace.place.friOpenHour.lastOrder,
                    QPlace.place.satOpenHour.open,
                    QPlace.place.satOpenHour.close,
                    QPlace.place.satOpenHour.lastOrder,
                    QPlace.place.sunOpenHour.open,
                    QPlace.place.sunOpenHour.close,
                    QPlace.place.sunOpenHour.lastOrder,
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

    @Transactional
    public void updatePlaceReviewInfo(final Long placeId, final long cntAdd, final long scoreAdd, final long quietAdd, final long accessibleAdd, final long safeAdd, final long scenicAdd, final long cleanAdd, final long comfortableAdd) {
        JPAUpdateClause updateClause = queryFactory.update(QPlace.place).where(QPlace.place.id.eq(placeId)).set(QPlace.place.reviewInfo.reviewCnt, QPlace.place.reviewInfo.reviewCnt.add(cntAdd)).set(QPlace.place.reviewInfo.totalScore, QPlace.place.reviewInfo.totalScore.add(scoreAdd));

        if (quietAdd != 0)
            updateClause.set(QPlace.place.reviewInfo.quietCnt, QPlace.place.reviewInfo.quietCnt.add(quietAdd));
        if (accessibleAdd != 0)
            updateClause.set(QPlace.place.reviewInfo.accessibleCnt, QPlace.place.reviewInfo.accessibleCnt.add(accessibleAdd));
        if (safeAdd != 0)
            updateClause.set(QPlace.place.reviewInfo.safeCnt, QPlace.place.reviewInfo.safeCnt.add(safeAdd));
        if (scenicAdd != 0)
            updateClause.set(QPlace.place.reviewInfo.scenicCnt, QPlace.place.reviewInfo.scenicCnt.add(scenicAdd));
        if (cleanAdd != 0)
            updateClause.set(QPlace.place.reviewInfo.cleanCnt, QPlace.place.reviewInfo.cleanCnt.add(cleanAdd));
        if (comfortableAdd != 0)
            updateClause.set(QPlace.place.reviewInfo.comfortableCnt, QPlace.place.reviewInfo.comfortableCnt.add(comfortableAdd));

        updateClause.execute();
        entityManager.refresh(entityManager.find(Place.class, placeId));
    }

    public Collection<PlaceTopBookmarkPercentageResponse> findPlaceTopBookmarkPercentageList(Collection<Long> placeIds) {
        List<Tuple> totalPlaceBookmarkCount = queryFactory.select(QPlaceBookmark.placeBookmark.place.id, count(all))
            .from(QPlaceBookmark.placeBookmark)
            .groupBy(QPlaceBookmark.placeBookmark.place.id)
            .orderBy(OrderByNull.DEFAULT)
            .stream()
            .sorted(Comparator.comparing(tuple -> tuple.get(1, Long.class), Comparator.reverseOrder()))
            .collect(Collectors.toList());

        return IntStream.range(0, totalPlaceBookmarkCount.size())
            .mapToObj(index -> new PlaceTopBookmarkPercentageResponse(
                totalPlaceBookmarkCount.get(index).get(0, Long.class),
                (double) index / (totalPlaceBookmarkCount.size() - 1))
            )
            .filter(placeTopBookmarkPercentageResponse -> placeIds.contains(placeTopBookmarkPercentageResponse.getPlaceId()))
            .collect(Collectors.toList());
    }
}
