package kr.co.pawpaw.mysql.chatroom.domain;

import kr.co.pawpaw.mysql.common.BaseTimeEntity;
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

    @JoinColumn(name = "chatroom_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Chatroom chatroom;

    @Column(name = "hash_tag", columnDefinition = "VARCHAR(2048) NOT NULL, FULLTEXT INDEX hash_tag_fulltext (hash_tag) WITH PARSER ngram")
    private String hashTag;

    @Builder
    public ChatroomHashTag(final Chatroom chatroom, final String hashTag) {
        this.chatroom = chatroom;
        this.hashTag = hashTag;
    }
}
