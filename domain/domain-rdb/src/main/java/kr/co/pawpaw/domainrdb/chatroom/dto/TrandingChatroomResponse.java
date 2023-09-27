package kr.co.pawpaw.domainrdb.chatroom.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class TrandingChatroomResponse {
    @Schema(description = "채팅방 아이디")
    private Long id;
    @Schema(description = "뜨고 있음 아이디")
    private Long trandingId;
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
    public TrandingChatroomResponse(
        final Long id,
        final Long trandingId,
        final String name,
        final String description,
        final List<String> hashTagList,
        final String managerName,
        final String managerImageUrl,
        final Long participantNumber
    ) {
        this.id = id;
        this.trandingId = trandingId;
        this.name = name;
        this.description = description;
        this.hashTagList = hashTagList;
        this.managerName = managerName;
        this.managerImageUrl = managerImageUrl;
        this.participantNumber = participantNumber;
    }
}
