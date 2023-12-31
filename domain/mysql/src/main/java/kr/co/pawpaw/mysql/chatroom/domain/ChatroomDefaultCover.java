package kr.co.pawpaw.mysql.chatroom.domain;

import kr.co.pawpaw.mysql.common.BaseTimeEntity;
import kr.co.pawpaw.mysql.storage.domain.File;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomDefaultCover extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private File coverFile;

    @Builder
    public ChatroomDefaultCover(final File coverFile) {
        this.coverFile = coverFile;
    }
}
