package kr.co.pawpaw.api.dto.chatroom;

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
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private List<String> hashTagList;
    @NotNull
    private Boolean searchable;
    @NotNull
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
