package kr.co.pawpaw.mysql.chatroom.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class TrendingChatroomResponse {
    @Schema(description = "채팅방 아이디")
    private Long id;
    @Schema(description = "뜨고 있음 아이디")
    private Long trendingId;
    @Schema(description = "채팅방 이름")
    private String name;
    @Schema(description = "채팅방 소개")
    private String description;
    @Schema(description = "채팅방 해시태그 목록")
    private List<String> hashTagList;
    @Schema(description = "채팅방 매니저 이름")
    private String managerName;
    @Schema(description = "채팅방 매니저 이미지 url")
    private String managerImageUrl;
    @Schema(description = "채팅방 참여자 수")
    private Long participantNumber;

    @QueryProjection
    public TrendingChatroomResponse(
        final Long id,
        final Long trendingId,
        final String name,
        final String description,
        final List<String> hashTagList,
        final String managerName,
        final String managerImageUrl,
        final Long participantNumber
    ) {
        this.id = id;
        this.trendingId = trendingId;
        this.name = name;
        this.description = description;
        this.hashTagList = hashTagList;
        this.managerName = managerName;
        this.managerImageUrl = managerImageUrl;
        this.participantNumber = participantNumber;
    }
}
