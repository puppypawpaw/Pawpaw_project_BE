package kr.co.pawpaw.mysql.storage.domain;

import lombok.Getter;

@Getter
public enum FileType {
    DEFAULT(false),
    CUSTOM(true);

    private final boolean needDelete;

    FileType(final boolean needDelete) {
        this.needDelete = needDelete;
    }
}
