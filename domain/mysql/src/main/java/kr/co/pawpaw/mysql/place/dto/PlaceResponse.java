package kr.co.pawpaw.mysql.place.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.mysql.common.dto.PositionResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class PlaceResponse {
    @Schema(description = "장소 아이디", example = "1")
    private Long id;
    @Schema(description = "이미지URL 목록", example = "[\"www.naver.com\",\"www.google.com\"]")
    private List<String> imageUrlList;
    @Schema(description = "장소 이름", example = "탑골 공원")
    private String name;
    private PositionResponse position;
    @Schema(description = "운영 시간", example = "토\\n▪\uFE0E\\t휴무\\n1,3주/토요일휴무\\t정기휴무 (매달 1, 3번째 토요일)\\n일\\n▪\uFE0E\\t정기휴무 (매주 일요일)\\n1,3주/토요일휴무\\t11:00 - 21:00\\n15:00 - 17:00 브레이크타임\\n20:30 라스트오더\\n월\\n▪\uFE0E\\t11:00 - 21:00\\n15:00 - 17:00 브레이크타임\\n20:30 라스트오더\\n1,3주/토요일휴무\\t11:00 - 21:00\\n15:00 - 17:00 브레이크타임\\n20:30 라스트오더\\n화\\n▪\uFE0E\\t11:00 - 21:00\\n15:00 - 17:00 브레이크타임\\n20:30 라스트오더\\n1,3주/토요일휴무\\t11:00 - 21:00\\n15:00 - 17:00 브레이크타임\\n20:30 라스트오더\\n수\\n▪\uFE0E\\t11:00 - 21:00\\n15:00 - 17:00 브레이크타임\\n20:30 라스트오더\\n1,3주/토요일휴무\\t11:00 - 21:00\\n15:00 - 17:00 브레이크타임\\n20:30 라스트오더\\n목\\n▪\uFE0E\\t11:00 - 21:00\\n15:00 - 17:00 브레이크타임\\n20:30 라스트오더\\n1,3주/토요일휴무\\t11:00 - 21:00\\n15:00 - 17:00 브레이크타임\\n20:30 라스트오더\\n금\\n▪\uFE0E\\t11:00 - 21:00\\n15:00 - 17:00 브레이크타임\\n20:30 라스트오더\\n1,3주/토요일휴무\\t11:00 - 21:00\\n15:00 - 17:00 브레이크타임\\n20:30 라스트오더")
    private String openHours;
    @Schema(description = "장소 북마크 여부", example = "true | false")
    private boolean bookmarked;
    @Schema(description = "평점", example = "4.8 | null")
    private Double score;
    @Schema(description = "조경좋음 비율", example = "0.9 | null")
    private Double scenicRatio;
    @Schema(description = "조용함 비율", example = "0.9 | null")
    private Double quietRatio;
    @Schema(description = "편안함 비율", example = "0.9 | null")
    private Double comfortableRatio;
    @Schema(description = "접근성 좋음 비율", example = "0.9 | null")
    private Double accessibleRatio;
    @Schema(description = "깨끗함 비율", example = "0.9 | null")
    private Double cleanRatio;
    @Schema(description = "안전함 비율", example = "0.9 | null")
    private Double safeRatio;

    @QueryProjection
    public PlaceResponse(
        final Long id,
        final List<String> imageUrlList,
        final String name,
        final PositionResponse position,
        final String openHours,
        final boolean bookmarked,
        final Double score,
        final Double scenicRatio,
        final Double quietRatio,
        final Double comfortableRatio,
        final Double accessibleRatio,
        final Double cleanRatio,
        final Double safeRatio
    ) {
        this.id = id;
        this.imageUrlList = imageUrlList;
        this.name = name;
        this.position = position;
        this.openHours = openHours;
        this.bookmarked = bookmarked;
        this.score = score;
        this.scenicRatio = scenicRatio;
        this.quietRatio = quietRatio;
        this.comfortableRatio = comfortableRatio;
        this.accessibleRatio = accessibleRatio;
        this.cleanRatio = cleanRatio;
        this.safeRatio = safeRatio;
    }
}
