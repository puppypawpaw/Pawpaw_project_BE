package kr.co.pawpaw.domainrdb.chatroom.domain;


import kr.co.pawpaw.domainrdb.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomHashTag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hashTag;
    @ManyToOne(fetch = FetchType.LAZY)
    private Chatroom chatroom;

    @Builder
    public ChatroomHashTag(
        final String hashTag,
        final Chatroom chatroom
    ) {
        this.hashTag = hashTag;
        this.chatroom = chatroom;
    }
}
