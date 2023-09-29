package kr.co.pawpaw.api.dto.reply;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class ReplyDto {

    @Getter
    @Schema(description = "댓글 등록 DTO")
    public static class ReplyRegisterDto {
        @Schema(name = "게시글 id", example = "1")
        private Long boardId;
        @Schema(name = "부모 댓글 id", nullable = true, example = "1")
        private Long parentId;
        @Schema(name = "댓글 내용", example = "댕댕이 귀엽네요")
        private String content;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(description = "댓글 수정 DTO")
    public static class ReplyUpdateDto {
        @Schema(name = "댓글 내용", example = "냥이 귀엽네요")
        private String content;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(description = "댓글 응답 DTO")
    public static class ReplyResponseDto {
        @Schema(name = "작성자 명")
        private String writer;
        @Schema(name = "게시글 id")
        private Long boardId;
        @Schema(name = "부모 댓글 id", nullable = true)
        private Long parentId;
        @Schema(name = "댓글 id")
        private Long replyId;
        @Schema(name = "댓글 내용")
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
    @Schema(description = "댓글 리스트 DTO")
    public static class ReplyListDto {
        @Schema(name = "댓글 id")
        private Long id;
        @Schema(name = "댓글 내용")
        private String content;
        @Schema(name = "작성자 명")
        private String nickname;
        @ArraySchema(schema = @Schema(description = "자식 댓글 리스트", nullable = true))
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
