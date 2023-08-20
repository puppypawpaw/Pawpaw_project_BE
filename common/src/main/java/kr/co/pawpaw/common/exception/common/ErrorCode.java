package kr.co.pawpaw.common.exception.common;

public enum ErrorCode {
    HANDLE_AUTHENTICATION_ENTRYPOINT(401, "C001", "로그인 후 사용 가능합니다."),
    METHOD_NOT_ALLOWED(405, "C002", "지원하지 않는 Method 입니다"),
    HANDLE_AUTHENTICATION_EXCEPTION(400, "C003", "잘못된 계정정보입니다."),
    CONSTRAINT_VALIDATION_EXCEPTION(400, "CV001", "잘못된 요청 파라미터 입니다."),
    DUPLICATE_ID(409, "U001", "중복된 아이디 입니다."),
    NOT_FOUND_USER(404, "U002", "존재하지 않는 유저입니다."),
    NOT_SIGNED_IN(400, "U003", "로그인 상태가 아닙니다."),
    NOT_FOUND_TERM(404, "T001", "존재하지 않는 약관입니다."),
    NOT_AGREE_ALL_REQUIRED_TERM(400, "T002", "모든 필수 약관에 동의가 필요합니다."),
    PERMISSION_REQUIRED(400, "PM001", "권한이 부족합니다."),
    NOT_EQUAL_PASSWORD_CONFIRM(400, "S001", "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    INVALID_PET_NAME(400, "P001", "유효하지 않은 반려동물 이름입니다."),
    INVALID_PET_INTRODUCTION(400, "P002", "유효하지 않은 반려동물 소개입니다."),
    INVALID_OAUTH2_TEMP_KEY(400, "OA001", "유효하지 않는 소셜 회원가입 임시 키입니다."),
    BOARD_NOTFOUND_EXCEPTION(400, "BOARD_NOTFOUND_EXCEPTION", "게시글을 찾지 못했습니다."),
    BOARD_REGISTRATION_EXCEPTION(400, "BOARD_REGISTRATION_EXCEPTION", "게시글 등록에 실패했습니다"),
    BOARD_UPDATE_EXCEPTION(400, "BOARD_UPDATE_EXCEPTION", "게시글 수정에 실패했습니다"),
    BOARD_DELETE_EXCEPTION(400, "BOARD_DELETE_EXCEPTION", "게시글 삭제에 실패했습니다");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(
        final int status,
        final String code,
        final String message
    ) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}