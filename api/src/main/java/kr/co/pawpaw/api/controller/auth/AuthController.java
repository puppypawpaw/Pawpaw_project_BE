package kr.co.pawpaw.api.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pawpaw.api.application.auth.*;
import kr.co.pawpaw.api.application.sms.SmsService;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.dto.auth.*;
import kr.co.pawpaw.api.dto.sms.CheckVerificationCodeRequest;
import kr.co.pawpaw.api.dto.sms.CheckVerificationCodeResponse;
import kr.co.pawpaw.api.dto.sms.SendVerificationCodeRequest;
import kr.co.pawpaw.api.dto.user.UserEmailResponse;
import kr.co.pawpaw.domainrdb.sms.domain.SmsUsagePurpose;
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
    private final SignOutService signOutService;
    private final SignUpService signUpService;
    private final SignInService signInService;
    private final SmsService smsService;
    private final ChangePasswordService changePasswordService;
    private final FindEmailService findEmailService;

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
            responseCode = "409",
            description = "이미 가입된 이메일 입니다.",
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
        signUpService.signUp(body, image);

        return ResponseEntity.noContent().build();
    }
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200")
    })
    @Operation(
        method = "GET",
        summary = "회원가입 시 이메일 중복확인",
        description = "이메일 중복 확인"
    )
    @GetMapping("/sign-up/check/duplicate/email")
    public ResponseEntity<DuplicateEmailResponse> checkDuplicateEmail(
        @RequestParam final String email
    ) {
        return ResponseEntity.ok(signUpService.checkDuplicateEmail(email));
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
            description = "유효하지 않은 소셜 회원가입 임시 키입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "POST",
        summary = "소셜 회원가입",
        description = "소셜회원가입, 회원가입 성공 시 자동 로그인됨, form-data 타입으로 body는 application/json으로 image는 contentType 지정 안해도 됨 추후 mediaType 제한될 수 있음"
    )
    @PostMapping(value = "/sign-up/social", consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<Void> socialSignUp(
        @RequestPart(required = false) final MultipartFile image,
        @RequestPart @Valid final SocialSignUpRequest body,
        final HttpServletResponse response
    ) {
        signInService.socialSignIn(response, signUpService.socialSignUp(body, image));

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
            responseCode = "404",
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
    public ResponseEntity<Void> signIn(
        @RequestBody @Valid final SignInRequest request,
        final HttpServletResponse response
    ) {
        signInService.signIn(response, request);
        return ResponseEntity.noContent().build();
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
        signOutService.signOut(userId, request, response);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "400",
            description = "유효하지 않은 소셜 회원가입 임시 키입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "GET",
        summary = "소셜 회원가입 이름, 이미지 url 가져오기",
        description = "소셜 회원가입 이름, 이미지 url 가져오기"
    )
    @GetMapping("/sign-up/social/info")
    public ResponseEntity<SocialSignUpInfoResponse> getOAuth2SignUpTempInfo(
        @RequestParam final String key
    ) {
        return ResponseEntity.ok(signUpService.getOAuth2SignUpTempInfo(key));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
    })
    @Operation(
        method = "POST",
        summary = "일반 회원가입 인증번호 전송",
        description = "일반 회원가입 인증번호 전송"
    )
    @PostMapping("/sign-up/verification")
    public ResponseEntity<Void> sendVerificationCodeForSignUp(
        @RequestBody final SendVerificationCodeRequest request
    ) {
        smsService.sendVerificationCode(request, SmsUsagePurpose.SIGN_UP);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "400",
            description = "유효하지 않은 인증 코드입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "POST",
        summary = "일반 회원가입 인증번호 확인",
        description = "일반 회원가입 인증번호 확인"
    )
    @PostMapping("/sign-up/verification/check")
    public ResponseEntity<CheckVerificationCodeResponse> checkVerificationCode(
        @RequestBody final CheckVerificationCodeRequest request
    ) {
        return ResponseEntity.ok(smsService.checkVerificationCode(request, SmsUsagePurpose.SIGN_UP));
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
        method = "POST",
        summary = "비밀번호 변경 url 전송",
        description = "비밀번호 변경 url 전송"
    )
    @PostMapping("/change/password/mail")
    public ResponseEntity<Void> sendChangePasswordMail(
        @RequestBody final ChangePasswordMailRequest request
    ) {
        changePasswordService.sendChangePasswordMail(request);

        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 비밀번호 변경 임시키입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "PATCH",
        summary = "비밀번호 변경",
        description = "비밀번호 변경"
    )
    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(
        @RequestBody final ChangePasswordRequest request
    ) {
        changePasswordService.changePassword(request);

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
        summary = "유저 이메일 찾기",
        description = "유저 이메일 찾기"
    )
    @GetMapping("/email")
    public ResponseEntity<UserEmailResponse> getUserEmail(
        @RequestParam final String name,
        @RequestParam final String phoneNumber
    ) {
        return ResponseEntity.ok(findEmailService.getUserEmail(name, phoneNumber));
    }
}
