package kr.co.pawpaw.api.controller.chatroom;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pawpaw.api.aop.ChatroomRoleCheck;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.dto.chatroom.ChatroomScheduleResponse;
import kr.co.pawpaw.api.dto.chatroom.CreateChatroomScheduleRequest;
import kr.co.pawpaw.api.dto.chatroom.CreateChatroomScheduleResponse;
import kr.co.pawpaw.api.service.chatroom.ChatroomScheduleService;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomParticipantRole;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "chatroom")
@RestController
@RequestMapping("/api/chatroom/{chatroomId}/schedule")
@RequiredArgsConstructor
public class ChatroomScheduleController {
    private final ChatroomScheduleService chatroomScheduleService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "400",
            description = "채팅방 참여자가 아닙니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "권한이 부족합니다.",
            content = @Content
        )
    })
    @Operation(
        method = "POST",
        summary = "채팅방 스케줄 생성 API",
        description = "채팅방 스케줄 생성 API"
    )
    @ChatroomRoleCheck
    @PostMapping
    public ResponseEntity<CreateChatroomScheduleResponse> createChatroomSchedule(
        @AuthenticatedUserId final UserId userId,
        @PathVariable(value = "chatroomId") final Long chatroomId,
        @RequestBody @Valid final CreateChatroomScheduleRequest request
    ) {
        return ResponseEntity.ok(chatroomScheduleService.createChatroomSchedule(userId, chatroomId, request));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "400",
            description = "채팅방 참여자가 아닙니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 채팅방 스케줄입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "POST",
        summary = "채팅방 스케줄 참가",
        description = "채팅방 스케줄 참가"
    )
    @ChatroomRoleCheck(role = ChatroomParticipantRole.PARTICIPANT)
    @PostMapping("/{scheduleId}/participant")
    public ResponseEntity<Void> participateChatroomSchedule(
        @AuthenticatedUserId final UserId userId,
        @PathVariable(value = "chatroomId") final Long chatroomId,
        @PathVariable(value = "scheduleId") final Long scheduleId
    ) {
        chatroomScheduleService.participateChatroomSchedule(userId, chatroomId, scheduleId);

        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "400",
            description = "채팅방 참여자가 아닙니다.",
            content = @Content
        )
    })
    @Operation(
        method = "GET",
        summary = "종료되지 않은 채팅방 스케줄 조회",
        description = "종료되지 않은 채팅방 스케줄 조회"
    )
    @ChatroomRoleCheck(role = ChatroomParticipantRole.PARTICIPANT)
    @GetMapping
    public ResponseEntity<List<ChatroomScheduleResponse>> getNotEndChatroomSchedule(
        @AuthenticatedUserId final UserId userId,
        @PathVariable(value = "chatroomId") final Long chatroomId
    ) {
        return ResponseEntity.ok(chatroomScheduleService.getNotEndChatroomScheduleList(userId, chatroomId));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "400",
            description = "채팅방 참여자가 아닙니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 채팅방 스케줄입니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "채팅방 스케줄 참여자가 아닙니다.",
            content = @Content
        )
    })
    @Operation(
        method = "DELETE",
        summary = "채팅방 스케줄 나가기",
        description = "채팅방 스케줄 나가기"
    )
    @ChatroomRoleCheck(role = ChatroomParticipantRole.PARTICIPANT)
    @DeleteMapping("/{scheduleId}/participant")
    public ResponseEntity<Void> leaveChatroomSchedule(
        @AuthenticatedUserId final UserId userId,
        @PathVariable(value = "chatroomId") final Long chatroomId,
        @PathVariable(value = "scheduleId") final Long scheduleId
    ) {
        chatroomScheduleService.leaveChatroomSchedule(userId, chatroomId, scheduleId);

        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "400",
            description = "채팅방 참여자가 아닙니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "권한이 부족합니다.",
            content = @Content
        )
    })
    @Operation(
        method = "DELETE",
        summary = "채팅방 스케줄 삭제하기",
        description = "채팅방 스케줄 삭제하기(방장만 가능)"
    )
    @ChatroomRoleCheck
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteChatroomSchedule(
        @AuthenticatedUserId final UserId userId,
        @PathVariable(value = "chatroomId") final Long chatroomId,
        @PathVariable(value = "scheduleId") final Long scheduleId
    ) {
        chatroomScheduleService.deleteChatroomSchedule(chatroomId, scheduleId);

        return ResponseEntity.noContent().build();
    }
}
