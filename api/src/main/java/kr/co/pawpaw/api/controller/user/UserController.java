package kr.co.pawpaw.api.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.dto.user.UpdateUserPositionRequest;
import kr.co.pawpaw.api.dto.user.UpdateUserProfileRequest;
import kr.co.pawpaw.api.dto.user.UserResponse;
import kr.co.pawpaw.api.service.user.UserService;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "user")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 유저입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "GET",
        summary = "유저 정보 가져오기",
        description = "유저 정보 가져오기"
    )
    @GetMapping
    public ResponseEntity<UserResponse> whoAmI(
        @AuthenticatedUserId final UserId userId
    ) {
        return ResponseEntity.ok(userService.whoAmI(userId));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
    })
    @Operation(
        method = "PUT",
        summary = "유저 유저 이미지 생성 또는 업데이트",
        description = "유저 유저 이미지 생성 또는 업데이트"
    )
    @PutMapping(
        value = "/image",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> updateUserImage(
        @AuthenticatedUserId final UserId userId,
        @RequestParam final MultipartFile file
    ) {
        userService.updateUserImage(userId, file);

        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode= "204"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 유저입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "PATCH",
        summary = "유저 프로필 수정",
        description = "유저 프로필 중 닉네임, 한줄 소개 수정"
    )
    @PatchMapping("/profile")
    public ResponseEntity<Void> updateUserProfile(
        @AuthenticatedUserId final UserId userId,
        @RequestBody final UpdateUserProfileRequest request
    ) {
        userService.updateUserProfile(userId, request);

        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 유저입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "PATCH",
        summary = "유저 위치 수정",
        description = "유저 위치 수정"
    )
    @PatchMapping("/position")
    public ResponseEntity<Void> updateUserPosition(
        @AuthenticatedUserId final UserId userId,
        @RequestBody final UpdateUserPositionRequest request
    ) {
        userService.updateUserPosition(userId, request);

        return ResponseEntity.noContent().build();
    }

}
