package kr.co.pawpaw.domainrdb.board.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BoardDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardRegisterDto {
        private Long id;
        private String title;
        private String content;
        private String writer;
        private List<String> fileNames;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardResponseDto {

        private String title;
        private String content;

        private String writer;

        private boolean isRemoved = false;

        private int viewCount;

        private int likedCount;

        private LocalDateTime createdAt = LocalDateTime.now();

        private LocalDateTime updatedAt = LocalDateTime.now();

        private List<String> commentList = new ArrayList<>();

        //첨부파일의 이름들
        private List<String> fileNames;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class BoardUpdateDto {
        private String title;
        private String content;
        private List<String> fileNames;

        @Builder
        public BoardUpdateDto(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }

}
