package kr.co.pawpaw.mysql.chatroom.domain;

import kr.co.pawpaw.mysql.common.BaseTimeEntity;
import kr.co.pawpaw.mysql.storage.domain.File;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatroom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(2048) NOT NULL, FULLTEXT INDEX name_fulltext (name) WITH PARSER ngram")
    private String name;
    @Column(columnDefinition = "VARCHAR(2048) NOT NULL, FULLTEXT INDEX description_fulltext (description) WITH PARSER ngram")
    private String description;
    @Column(nullable = false)
    private Boolean searchable;
    @Column(nullable = false)
    private Boolean locationLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    private File coverFile;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatroomParticipant manager;

    @OneToMany(mappedBy = "chatroom")
    private final List<ChatroomParticipant> chatroomParticipants = new ArrayList<>();

    @Builder
    public Chatroom(
        final String name,
        final String description,
        final Boolean searchable,
        final Boolean locationLimit,
        final File coverFile
    ) {
        this.name = name;
        this.description = description;
        this.searchable = searchable;
        this.locationLimit = locationLimit;
        this.coverFile = coverFile;
    }

    public void updateManager(final ChatroomParticipant manager) {
        this.manager = manager;
    }
}
