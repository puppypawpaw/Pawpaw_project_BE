package kr.co.pawpaw.api.controller.chatroom;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pawpaw.api.application.chatroom.ChatroomScheduleService;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.dto.chatroom.ChatroomScheduleResponse;
import kr.co.pawpaw.api.dto.chatroom.CreateChatroomScheduleRequest;
import kr.co.pawpaw.api.dto.chatroom.CreateChatroomScheduleResponse;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
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
            description = "채팅방 참석자가 아닙니다.",
            content = @Content
        ),
    })
    @Operation(
        method = "POST",
        summary = "채팅방 스케줄 생성 API",
        description = "채팅방 스케줄 생성 API"
    )
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
            description = "채팅방 참석자가 아닙니다.",
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
    @PostMapping("/{scheduleId}")
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
            description = "채팅방 참석자가 아닙니다.",
            content = @Content
        )
    })
    @Operation(
        method = "GET",
        summary = "종료되지 않은 채팅방 스케줄 조회",
        description = "종료되지 않은 채팅방 스케줄 조회"
    )
    @GetMapping
    public ResponseEntity<List<ChatroomScheduleResponse>> getNotEndChatroomSchedule(
        @AuthenticatedUserId final UserId userId,
        @PathVariable(value = "chatroomId") final Long chatroomId
    ) {
        return ResponseEntity.ok(chatroomScheduleService.getNotEndChatroomScheduleList(userId, chatroomId));
    }
}
