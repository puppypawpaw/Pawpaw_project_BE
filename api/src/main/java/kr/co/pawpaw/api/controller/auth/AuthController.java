package kr.co.pawpaw.api.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pawpaw.api.application.auth.AuthService;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.dto.auth.SignInRequest;
import kr.co.pawpaw.api.dto.auth.SignUpRequest;
import kr.co.pawpaw.api.dto.auth.SocialSignUpRequest;
import kr.co.pawpaw.api.dto.user.UserResponse;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "auth")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "409",
            description = "중복된 아이디 입니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "모든 필수 약관에 동의가 필요합니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "비밀번호와 비밀번호 확인이 일치하지 않습니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 파라미터입니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "중복된 아이디 입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "POST",
        summary = "회원가입",
        description = "회원가입, form-data 타입으로 body는 application/json으로 image는 contentType 지정 안해도 됨 추후 mediaType 제한될 수 있음"
    )
    @PostMapping(value = "/sign-up", consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<Void> signUp(
        @RequestPart(required = false) final MultipartFile image,
        @RequestPart @Valid final SignUpRequest body
    ) {
        authService.signUp(body, image);

        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "400",
            description = "모든 필수 약관에 동의가 필요합니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 파라미터입니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "유효하지 않는 소셜 회원가입 임시 키입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "POST",
        summary = "소셜 회원가입",
        description = "소셜회원가입, form-data 타입으로 body는 application/json으로 image는 contentType 지정 안해도 됨 추후 mediaType 제한될 수 있음"
    )
    @PostMapping(value = "/sign-up/social", consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<Void> socialSignUp(
        @RequestPart(required = false) final MultipartFile image,
        @RequestPart @Valid final SocialSignUpRequest body
    ) {
        authService.socialSignUp(body, image);

        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 계정정보입니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "존재하지 않는 유저입니다.",
            content = @Content
        )
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
        return ResponseEntity.ok(authService.signIn(response, request));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "400",
            description = "로그인 상태가 아닙니다.",
            content = @Content
        )
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
        authService.signOut(userId, request, response);
        return ResponseEntity.noContent().build();
    }

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
        summary = "회원정보 가져오기",
        description = "회원정보 가져오기"
    )
    @GetMapping
    public ResponseEntity<UserResponse> whoAmI(
        @AuthenticatedUserId final UserId userId
    ) {
        return ResponseEntity.ok(authService.whoAmI(userId));
    }
}
