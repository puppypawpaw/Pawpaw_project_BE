package kr.co.pawpaw.api.dto.reply;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class ReplyDto {

    @Getter
    public static class ReplyRegisterDto {
        private Long boardId;
        private Long parentId;
        private String content;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReplyUpdateDto {
        private String content;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReplyResponseDto {
        private String writer;
        private Long boardId;
        private Long parentId;
        private Long replyId;
        private String content;

        @Builder
        public ReplyResponseDto(String writer, Long boardId, Long parentId, Long replyId, String content) {
            this.writer = writer;
            this.boardId = boardId;
            this.parentId = parentId;
            this.replyId = replyId;
            this.content = content;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReplyListDto {
        private Long id;
        private String content;
        private String nickname;
        private List<ReplyListDto> children = new ArrayList<>();

        public ReplyListDto(Long id, String content, String nickname) {
            this.id = id;
            this.content = content;
            this.nickname = nickname;
        }
        public void setChildToParentReply(List<ReplyListDto> children){
            this.children = children;
        }
    }
}
