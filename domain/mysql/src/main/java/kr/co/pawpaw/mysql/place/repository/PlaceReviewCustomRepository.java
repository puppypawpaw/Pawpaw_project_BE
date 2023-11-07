package kr.co.pawpaw.mysql.place.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.ResultTransformer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pawpaw.mysql.place.domain.QPlaceReview;
import kr.co.pawpaw.mysql.place.domain.QPlaceReviewImage;
import kr.co.pawpaw.mysql.place.dto.PlaceReviewResponse;
import kr.co.pawpaw.mysql.place.dto.QPlaceReviewImageResponse;
import kr.co.pawpaw.mysql.place.dto.QPlaceReviewResponse;
import kr.co.pawpaw.mysql.storage.domain.QFile;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@RequiredArgsConstructor
public class PlaceReviewCustomRepository {
    private final JPAQueryFactory queryFactory;

    public PlaceReviewResponse findByPlaceIdAndReviewerUserId(
        final Long placeId,
        final UserId userId
    ) {
        List<PlaceReviewResponse> result = queryFactory
            .from(QPlaceReview.placeReview)
            .innerJoin(QPlaceReview.placeReview.reviewer)
            .innerJoin(QPlaceReview.placeReview.reviewer.userImage)
            .leftJoin(QPlaceReview.placeReview.placeReviewImageList, QPlaceReviewImage.placeReviewImage)
            .leftJoin(QPlaceReviewImage.placeReviewImage.image, QFile.file)
            .where(QPlaceReview.placeReview.place.id.eq(placeId)
                .and(QPlaceReview.placeReview.reviewer.userId.eq(userId)))
            .transform(getGroupByResultTransformer());

        return result.size() > 0 ? result.get(0) : null;
    }

    public Slice<PlaceReviewResponse> findByPlaceIdAndIdBefore(
        final UserId userId,
        final Long placeId,
        final Long beforeReviewId,
        final int size
    ) {
        List<Long> coveringIndex = getCoveringIndex(userId, placeId, beforeReviewId, size);

        List<PlaceReviewResponse> result = queryFactory
            .from(QPlaceReview.placeReview)
            .innerJoin(QPlaceReview.placeReview.reviewer)
            .innerJoin(QPlaceReview.placeReview.reviewer.userImage)
            .leftJoin(QPlaceReview.placeReview.placeReviewImageList, QPlaceReviewImage.placeReviewImage)
            .leftJoin(QPlaceReviewImage.placeReviewImage.image, QFile.file)
            .where(QPlaceReview.placeReview.id.in(coveringIndex.subList(0, Math.min(coveringIndex.size(), size))))
            .orderBy(QPlaceReview.placeReview.id.desc())
            .transform(getGroupByResultTransformer());

        return new SliceImpl<>(result, PageRequest.of(0, size), coveringIndex.size() > size);
    }

    private static ResultTransformer<List<PlaceReviewResponse>> getGroupByResultTransformer() {
        return groupBy(QPlaceReview.placeReview.id)
            .list(new QPlaceReviewResponse(
                QPlaceReview.placeReview.id,
                QPlaceReview.placeReview.reviewer.userId,
                QPlaceReview.placeReview.reviewer.nickname,
                QPlaceReview.placeReview.reviewer.briefIntroduction,
                QPlaceReview.placeReview.reviewer.userImage.fileUrl,
                list(new QPlaceReviewImageResponse(
                    QPlaceReviewImage.placeReviewImage.id,
                    QFile.file.fileUrl
                ).skipNulls()),
                QPlaceReview.placeReview.score,
                QPlaceReview.placeReview.isScenic,
                QPlaceReview.placeReview.isQuiet,
                QPlaceReview.placeReview.isComfortable,
                QPlaceReview.placeReview.isAccessible,
                QPlaceReview.placeReview.isClean,
                QPlaceReview.placeReview.isSafe,
                QPlaceReview.placeReview.content
            ));
    }

    private List<Long> getCoveringIndex(
        final UserId userId,
        final Long placeId,
        final Long beforeReviewId,
        final int size
    ) {
        BooleanBuilder condition = new BooleanBuilder()
            .and(QPlaceReview.placeReview.place.id.eq(placeId))
            .and(QPlaceReview.placeReview.reviewer.userId.ne(userId));

        if (Objects.nonNull(beforeReviewId)) {
            condition.and(QPlaceReview.placeReview.id.lt(beforeReviewId));
        }

        return queryFactory.select(QPlaceReview.placeReview.id)
            .from(QPlaceReview.placeReview)
            .where(condition)
            .limit(size + 1)
            .fetch();
    }
}
