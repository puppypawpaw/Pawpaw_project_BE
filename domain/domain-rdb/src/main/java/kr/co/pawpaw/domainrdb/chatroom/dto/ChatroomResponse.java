package kr.co.pawpaw.domainrdb.chatroom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
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
}
