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

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Boolean searchable;
    @Column(nullable = false)
    private Boolean locationLimit;

    @Type(type = "io.hypersistence.utils.hibernate.type.json.JsonType")
    @Column(columnDefinition = "TEXT")
    private final List<String> hashTagList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private File coverFile;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatroomParticipant manager;

    @OneToMany(mappedBy = "chatroom")
    private final List<ChatroomParticipant> chatroomParticipants = new ArrayList<>();

    @Builder
    public Chatroom(
        final Collection<String> hashTagList,
        final String name,
        final String description,
        final Boolean searchable,
        final Boolean locationLimit,
        final File coverFile
    ) {
        if (Objects.nonNull(hashTagList)) {
            this.hashTagList.addAll(hashTagList);
        }
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
