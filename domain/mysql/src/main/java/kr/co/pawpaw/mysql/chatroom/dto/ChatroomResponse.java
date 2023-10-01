package kr.co.pawpaw.mysql.chatroom.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatroomResponse {
    @Schema(description = "채팅방 아이디")
    private Long id;
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
    public ChatroomResponse(
        final Long id,
        final String name,
        final String description,
        final List<String> hashTagList,
        final String managerName,
        final String managerImageUrl,
        final Long participantNumber
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.hashTagList = hashTagList;
        this.managerName = managerName;
        this.managerImageUrl = managerImageUrl;
        this.participantNumber = participantNumber;
    }
}
