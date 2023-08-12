package com.puppy.pawpaw_project_be.controller.auth;

import com.puppy.pawpaw_project_be.application.auth.command.SignService;
import com.puppy.pawpaw_project_be.application.auth.query.AuthQuery;
import com.puppy.pawpaw_project_be.config.annotation.AuthenticatedUserId;
import com.puppy.pawpaw_project_be.domain.auth.dto.request.SignInRequest;
import com.puppy.pawpaw_project_be.domain.auth.dto.request.SignUpRequest;
import com.puppy.pawpaw_project_be.domain.user.domain.UserId;
import com.puppy.pawpaw_project_be.domain.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "409", description = "중복된 아이디 입니다."),
        @ApiResponse(responseCode = "400", description = "모든 필수 약관에 동의가 필요합니다."),
        @ApiResponse(responseCode = "400", description = "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
        @ApiResponse(responseCode = "400", description = "유효하지 않은 반려동물 이름입니다."),
        @ApiResponse(responseCode = "400", description = "유효하지 않은 반려동물 소개입니다."),
        @ApiResponse(responseCode = "400", description = "중복된 아이디 입니다.")
    })
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

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", description = "잘못된 계정정보입니다."),
        @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.")
    })
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

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "400", description = "로그인 상태가 아닙니다.")
    })
    @Operation(
        method = "DELETE",
        summary = "로그아웃",
        description = "로그아웃"
    )
    @DeleteMapping
    public ResponseEntity<Void> signOut(
        final HttpServletRequest request,
        final HttpServletResponse response,
        @AuthenticatedUserId final UserId userId
    ) {
        signService.signOut(userId, request, response);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.")
    })
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
