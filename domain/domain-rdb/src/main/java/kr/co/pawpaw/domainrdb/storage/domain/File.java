package kr.co.pawpaw.domainrdb.storage.domain;

import kr.co.pawpaw.domainrdb.common.BaseTimeEntity;
import kr.co.pawpaw.domainrdb.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
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

    @ManyToOne(fetch = FetchType.LAZY)
    private User uploader;

    @Builder
    public File(
        final String fileName,
        final String fileUrl,
        final String contentType,
        final Long byteSize,
        final User uploader
    ) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.contentType = contentType;
        this.byteSize = byteSize;
        this.uploader = uploader;
    }
}
