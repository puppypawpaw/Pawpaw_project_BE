package kr.co.pawpaw.domainrdb.storage.domain;

import kr.co.pawpaw.domainrdb.common.BaseTimeEntity;
import kr.co.pawpaw.domainrdb.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends BaseTimeEntity {
    @Id
    private String fileName;
    private String contentType;
    private Long byteSize;

    @ManyToOne(fetch = FetchType.LAZY)
    private User uploader;

    @Builder
    public File(
        final String contentType,
        final Long byteSize,
        final User uploader
    ) {
        this.fileName = UUID.randomUUID().toString();
        this.contentType = contentType;
        this.byteSize = byteSize;
        this.uploader = uploader;
    }
}
