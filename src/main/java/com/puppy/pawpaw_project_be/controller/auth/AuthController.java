package com.puppy.pawpaw_project_be.controller.auth;

import com.puppy.pawpaw_project_be.application.auth.command.SignService;
import com.puppy.pawpaw_project_be.application.auth.query.AuthQuery;
import com.puppy.pawpaw_project_be.config.annotation.AuthenticatedUserId;
import com.puppy.pawpaw_project_be.domain.auth.dto.request.SignInRequest;
import com.puppy.pawpaw_project_be.domain.auth.dto.request.SignUpRequest;
import com.puppy.pawpaw_project_be.domain.user.domain.UserId;
import com.puppy.pawpaw_project_be.domain.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "auth")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final SignService signService;
    private final AuthQuery authQuery;

    @ApiResponse(responseCode = "204")
    @Operation(
        method = "POST",
        summary = "회원가입",
        description = "회원가입"
    )
    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(
        @RequestBody @Valid final SignUpRequest request
    ) {
        signService.signUp(request);

        return ResponseEntity.noContent().build();
    }

    @ApiResponse(responseCode = "200")
    @Operation(
        method = "POST",
        summary = "로그인",
        description = "로그인"
    )
    @PostMapping
    public ResponseEntity<UserResponse> signIn(
        @RequestBody @Valid final SignInRequest request,
        final HttpServletResponse response
    ) {
        return ResponseEntity.ok(signService.signIn(response, request));
    }

    @ApiResponse(responseCode = "204")
    @Operation(
        method = "DELETE",
        summary = "로그아웃",
        description = "로그아웃"
    )
    @DeleteMapping
    public ResponseEntity<Void> signOut(
        final HttpServletRequest request,
        final HttpServletResponse response,
        @Schema(hidden = true) @AuthenticatedUserId final UserId userId
    ) {
        signService.signOut(userId, request, response);
        return ResponseEntity.noContent().build();
    }

    @ApiResponse(responseCode = "200")
    @Operation(
        method = "GET",
        summary = "회원정보 가져오기",
        description = "회원정보 가져오기"
    )
    @GetMapping
    public ResponseEntity<UserResponse> whoAmI(
        @AuthenticatedUserId final UserId userId
    ) {
        return ResponseEntity.ok(authQuery.whoAmI(userId));
    }
}
