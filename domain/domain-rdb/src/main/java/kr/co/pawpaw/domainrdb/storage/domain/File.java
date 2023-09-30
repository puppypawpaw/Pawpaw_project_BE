package kr.co.pawpaw.domainrdb.storage.domain;

import kr.co.pawpaw.domainrdb.common.BaseTimeEntity;
import kr.co.pawpaw.domainrdb.user.domain.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends BaseTimeEntity {
    @Id
    private String fileName;

    @Column(nullable = false)
    private String fileUrl;
    @Column(nullable = false)
    private String contentType;
    @Column(nullable = false)
    private Long byteSize;
    @Builder.Default @Enumerated(value = EnumType.STRING)
    @Column(nullable = false) @ColumnDefault(value = "'CUSTOM'")
    private FileType type = FileType.CUSTOM;
    @ManyToOne(fetch = FetchType.LAZY)
    private User uploader;
}
