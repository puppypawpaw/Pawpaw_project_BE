package kr.co.pawpaw.api.dto.chatroom;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomHashTag;
import kr.co.pawpaw.domainrdb.user.domain.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

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

    public Chatroom toChatroom() {
        return Chatroom.builder()
            .name(name)
            .description(description)
            .searchable(searchable)
            .locationLimit(locationLimit)
            .build();
    }

    public List<ChatroomHashTag> toChatroomHashTags(final Chatroom chatroom) {
        return hashTagList.stream()
            .map(hashTag -> ChatroomHashTag.builder()
                .chatroom(chatroom)
                .hashTag(hashTag)
                .build())
            .collect(Collectors.toList());
    }
}
