package kr.co.pawpaw.mysql.chatroom.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Collection;

@Getter
public class ChatroomResponse {
    @Schema(description = "채팅방 아이디", example = "1")
    private Long id;
    @Schema(description = "채팅방 이름", example = "천하제일 내 반려동물 자랑방")
    private String name;
    @Schema(description = "채팅방 소개", example = "반려동물을 키우는 사람이라면 누구나 들어와서 자랑해주세요~")
    private String description;
    @Schema(description = "채팅방 해시태그 목록", example = "[\"강아지\",\"고양이\"]")
    private Collection<String> hashTagList;
    @Schema(description = "채팅방 매니저 이름", example = "지상최강감자")
    private String managerName;
    @Schema(description = "채팅방 매니저 이미지 url", example = "https://example.com")
    private String managerImageUrl;
    @Schema(description = "채팅방 참여자 수", example = "2")
    private Long participantNumber;

    @QueryProjection
    public ChatroomResponse(
        final Long id,
        final String name,
        final String description,
        final Collection<String> hashTagList,
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
