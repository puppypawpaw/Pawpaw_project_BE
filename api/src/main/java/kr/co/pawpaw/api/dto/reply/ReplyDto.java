package kr.co.pawpaw.api.dto.reply;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReplyDto {

    @Getter
    @Schema(description = "댓글 등록 DTO")
    public static class ReplyRegisterDto {
        @Schema(description = "게시글 id", example = "1")
        private Long boardId;
        @Schema(description = "부모 댓글 id", nullable = true, example = "1")
        private Long parentId;
        @Schema(description = "댓글 내용", example = "댕댕이 귀엽네요")
        private String content;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(description = "댓글 수정 DTO")
    public static class ReplyUpdateDto {
        @Schema(description = "댓글 내용", example = "냥이 귀엽네요")
        private String content;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(description = "댓글 응답 DTO")
    public static class ReplyResponseDto {
        @Schema(description = "작성자 명", example = "냥이최고")
        private String writer;
        @Schema(description = "게시글 id", example = "12")
        private Long boardId;
        @Schema(description = "부모 댓글 id", nullable = true, example = "1")
        private Long parentId;
        @Schema(description = "댓글 id", example = "10")
        private Long replyId;
        @Schema(description = "댓글 내용", example = "우리집 고양이가 최고야")
        private String content;
        @Schema(description = "댓글 생성일자", example = "2023-10-17 15:01:36.464359")
        private LocalDateTime createdDate;
        @Schema(description = "댓글 수정일자", example = "2023-10-17 15:01:36.464359")
        private LocalDateTime modifiedDate;

        @Builder
        public ReplyResponseDto(String writer, Long boardId, Long parentId, Long replyId, String content, LocalDateTime createdDate, LocalDateTime modifiedDate) {
            this.writer = writer;
            this.boardId = boardId;
            this.parentId = parentId;
            this.replyId = replyId;
            this.content = content;
            this.createdDate = createdDate;
            this.modifiedDate = modifiedDate;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(description = "댓글 리스트 DTO")
    public static class ReplyListDto {
        @Schema(description = "댓글 id", example = "21")
        private Long id;
        @Schema(description = "댓글 작성자 id", example = "4b067a59-1d81-4d3b-a58f-0843ac1eaa84")
        private UserId replyWriterId;
        @Schema(description = "댓글 내용", example = "우리집 댕댕이가 최고야")
        private String content;
        @Schema(description = "작성자 명", example = "냥이최고")
        private String nickname;
        @Schema(description = "댓글 작성자 확인", example = "true")
        private boolean replyWriter;
        @Schema(description = "유저 프로필 url", example = "https://pub-8a7cf54e8cb44eabac95682c0d8fcc42.r2.dev/유저 기본 이미지.jpg")
        private String userImageUrl;
        @ArraySchema(schema = @Schema(description = "자식 댓글 리스트", nullable = true))
        private List<ReplyListDto> children = new ArrayList<>();
        @Schema(description = "댓글 생성일자", example = "2023-10-17 15:01:36.464359")
        private LocalDateTime createdDate;
        @Schema(description = "댓글 수정일자", example = "2023-10-17 15:01:36.464359")
        private LocalDateTime modifiedDate;

        @Builder
        public ReplyListDto(Long id, UserId replyWriterId, String content, String nickname, boolean replyWriter, String userImageUrl,
                            List<ReplyListDto> childToParentReply, LocalDateTime createdDate, LocalDateTime modifiedDate) {
            this.id = id;
            this.replyWriterId = replyWriterId;
            this.content = content;
            this.nickname = nickname;
            this.replyWriter = replyWriter;
            this.userImageUrl = userImageUrl;
            this.children = childToParentReply;
            this.createdDate = createdDate;
            this.modifiedDate = modifiedDate;
        }
    }
}
