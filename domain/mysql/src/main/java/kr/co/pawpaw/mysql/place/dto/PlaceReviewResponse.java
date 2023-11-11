package kr.co.pawpaw.mysql.place.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.Getter;

import java.util.List;

@Getter
public class PlaceReviewResponse {
    @Schema(description = "장소 리뷰 아이디", example = "1")
    private Long placeReviewId;
    @Schema(description = "장소 리뷰 작성자 아이디", example = "12246988-e131-47e9-9f1f-ba754b897316")
    private UserId reviewerId;
    @Schema(description = "장소 리뷰 작성자 닉네임", example = "애옹맘")
    private String reviewerNickname;
    @Schema(description = "장소 리뷰 작성자 한줄소개", example = "2살 초코")
    private String reviewerBriefIntroduction;
    @Schema(description = "장소 리뷰 작성자 이미지 URL", example = "https://image.com")
    private String reviewerImageUrl;
    @Schema(description = "장소 리뷰 이미지 응답 목록")
    private List<PlaceReviewImageResponse> placeReviewImageList;
    @Schema(description = "장소 리뷰 점수", example = "5")
    private Long score;
    @Schema(description = "장소 리뷰 - 조경좋음 여부", type = "BOOLEAN", example = "true")
    private boolean isScenic;
    @Schema(description = "장소 리뷰 - 조용함 여부", type = "BOOLEAN", example = "true")
    private boolean isQuiet;
    @Schema(description = "장소 리뷰 - 쾌적함 여부", type = "BOOLEAN", example = "true")
    private boolean isComfortable;
    @Schema(description = "장소 리뷰 - 접근성 여부", type = "BOOLEAN", example = "true")
    private boolean isAccessible;
    @Schema(description = "장소 리뷰 - 깨끗함 여부", type = "BOOLEAN", example = "true")
    private boolean isClean;
    @Schema(description = "장소 리뷰 - 안전함 여부", type = "BOOLEAN", example = "true")
    private boolean isSafe;
    @Schema(description = "장소 리뷰 내용", example = "AI 추천이랑 잘 맞는다고 뜨길래 친구 꼬셔서 한번 방문해보았습니다~ 역시 쾌적하고 좋더라구요! 강아지 산책도 많이 시키고 있던데 숨겨진 핫플인가 싶었어요. 친구네 강아지도 신났는지 여기저기 엄청 돌아다녔어요 한번 꼭 방문해보세요! 추천합니다!!")
    private String content;

    @QueryProjection
    public PlaceReviewResponse(
        final Long placeReviewId,
        final UserId reviewerId,
        final String reviewerNickname,
        final String reviewerBriefIntroduction,
        final String reviewerImageUrl,
        final List<PlaceReviewImageResponse> placeReviewImageList,
        final Long score,
        final boolean isScenic,
        final boolean isQuiet,
        final boolean isComfortable,
        final boolean isAccessible,
        final boolean isClean,
        final boolean isSafe,
        final String content
    ) {
        this.placeReviewId = placeReviewId;
        this.reviewerId = reviewerId;
        this.reviewerNickname = reviewerNickname;
        this.reviewerBriefIntroduction = reviewerBriefIntroduction;
        this.reviewerImageUrl = reviewerImageUrl;
        this.placeReviewImageList = placeReviewImageList;
        this.score = score;
        this.isScenic = isScenic;
        this.isQuiet = isQuiet;
        this.isComfortable = isComfortable;
        this.isAccessible = isAccessible;
        this.isClean = isClean;
        this.isSafe = isSafe;
        this.content = content;
    }
}
