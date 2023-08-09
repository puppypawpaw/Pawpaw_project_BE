package com.puppy.pawpaw_project_be.exception.common;

public enum ErrorCode {
    HANDLE_AUTHENTICATION_ENTRYPOINT(401, "C001", "로그인 후 사용 가능합니다."),
    METHOD_NOT_ALLOWED(405, "C002", "지원하지 않는 Method 입니다"),
    HANDLE_AUTHENTICATION_EXCEPTION(400, "C003", "잘못된 계정정보입니다."),
    DUPLICATE_ID(409, "U001", "중복된 아이디 입니다."),
    NOT_FOUND_USER(404, "U002", "존재하지 않는 유저입니다."),
    NOT_SIGNED_IN(400, "U003", "로그인 상태가 아닙니다."),
    NOT_FOUND_TERM(404, "T001", "존재하지 않는 약관입니다."),
    PERMISSION_REQUIRED(400, "P001", "권한이 부족합니다.");

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
