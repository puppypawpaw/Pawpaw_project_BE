package kr.co.pawpaw.mysql.place.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewInfo {
    private long reviewCnt;
    private long totalScore;
    private long scenicCnt;
    private long quietCnt;
    private long comfortableCnt;
    private long accessibleCnt;
    private long cleanCnt;
    private long safeCnt;
}
