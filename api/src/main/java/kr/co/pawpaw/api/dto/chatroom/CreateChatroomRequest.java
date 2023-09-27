package kr.co.pawpaw.api.dto.chatroom;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateChatroomRequest {
    @NotBlank
    @Schema(description = "채팅방 이름", example = "한강 산책")
    private String name;
    @NotBlank
    @Schema(description = "채팅방 설명", example = "한강 산책 같이 가요")
    private String description;
    @NotNull
    @Schema(description = "채팅방 해시태그 목록", example = "['한강', '산책']")
    private List<String> hashTagList;
    @NotNull
    @Schema(description = "검색 가능 여부", example = "true")
    private Boolean searchable;
    @NotNull
    @Schema(description = "장소 제한 여부", example = "true")
    private Boolean locationLimit;

    public Chatroom toChatroom(final File coverFile) {
        return Chatroom.builder()
            .name(name)
            .description(description)
            .searchable(searchable)
            .locationLimit(locationLimit)
            .coverFile(coverFile)
            .hashTagList(hashTagList)
            .build();
    }
}
