package kr.co.pawpaw.mysql.place.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.mysql.common.dto.PositionResponse;
import lombok.Getter;

import java.util.Set;

@Getter
public class PlaceResponse {
    @Schema(description = "장소 아이디", example = "1")
    private Long id;
    @Schema(description = "이미지URL 목록", example = "[\"www.naver.com\",\"www.google.com\"]")
    private Set<String> imageUrlList;
    @Schema(description = "장소 이름", example = "탑골 공원")
    private String name;
    private PositionResponse position;
    @Schema(description = "월요일 open 시간", example = "10:00")
    private String monOpen;
    @Schema(description = "월요일 close 시간", example = "23:00")
    private String monClose;
    @Schema(description = "월요일 라스트 오더 시간", example = "22:30")
    private String monLastOrder;
    @Schema(description = "화요일 open 시간", example = "10:00")
    private String tueOpen;
    @Schema(description = "화요일 close 시간", example = "23:00")
    private String tueClose;
    @Schema(description = "화요일 라스트 오더 시간", example = "22:30")
    private String tueLastOrder;
    @Schema(description = "수요일 open 시간", example = "10:00")
    private String wedOpen;
    @Schema(description = "수요일 close 시간", example = "23:00")
    private String wedClose;
    @Schema(description = "수요일 라스트 오더 시간", example = "22:30")
    private String wedLastOrder;
    @Schema(description = "목요일 open 시간", example = "10:00")
    private String thuOpen;
    @Schema(description = "목요일 close 시간", example = "23:00")
    private String thuClose;
    @Schema(description = "목요일 라스트 오더 시간", example = "22:30")
    private String thuLastOrder;
    @Schema(description = "금요일 open 시간", example = "10:00")
    private String friOpen;
    @Schema(description = "금요일 close 시간", example = "23:00")
    private String friClose;
    @Schema(description = "금요일 라스트 오더 시간", example = "22:30")
    private String friLastOrder;
    @Schema(description = "토요일 open 시간", example = "10:00")
    private String satOpen;
    @Schema(description = "토요일 close 시간", example = "23:00")
    private String satClose;
    @Schema(description = "토요일 라스트 오더 시간", example = "22:30")
    private String satLastOrder;
    @Schema(description = "일요일 open 시간", example = "10:00")
    private String sunOpen;
    @Schema(description = "일요일 close 시간", example = "23:00")
    private String sunClose;
    @Schema(description = "일요일 라스트 오더 시간", example = "22:30")
    private String sunLastOrder;
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
        final Set<String> imageUrlList,
        final String name,
        final PositionResponse position,
        final String monOpen,
        final String monClose,
        final String monLastOrder,
        final String tueOpen,
        final String tueClose,
        final String tueLastOrder,
        final String wedOpen,
        final String wedClose,
        final String wedLastOrder,
        final String thuOpen,
        final String thuClose,
        final String thuLastOrder,
        final String friOpen,
        final String friClose,
        final String friLastOrder,
        final String satOpen,
        final String satClose,
        final String satLastOrder,
        final String sunOpen,
        final String sunClose,
        final String sunLastOrder,
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
        this.monOpen = monOpen;
        this.monClose = monClose;
        this.monLastOrder = monLastOrder;
        this.tueOpen = tueOpen;
        this.tueClose = tueClose;
        this.tueLastOrder = tueLastOrder;
        this.wedOpen = wedOpen;
        this.wedClose = wedClose;
        this.wedLastOrder = wedLastOrder;
        this.thuOpen = thuOpen;
        this.thuClose = thuClose;
        this.thuLastOrder = thuLastOrder;
        this.friOpen = friOpen;
        this.friClose = friClose;
        this.friLastOrder = friLastOrder;
        this.satOpen = satOpen;
        this.satClose = satClose;
        this.satLastOrder = satLastOrder;
        this.sunOpen = sunOpen;
        this.sunClose = sunClose;
        this.sunLastOrder = sunLastOrder;
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
