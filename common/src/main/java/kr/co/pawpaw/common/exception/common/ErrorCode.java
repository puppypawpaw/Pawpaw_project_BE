package kr.co.pawpaw.common.exception.common;

public enum ErrorCode {
    // 기본
    HANDLE_AUTHENTICATION_ENTRYPOINT(401, "C001", "로그인 후 사용 가능합니다."),
    METHOD_NOT_ALLOWED(405, "C002", "지원하지 않는 Method 입니다"),
    HANDLE_AUTHENTICATION_EXCEPTION(400, "C003", "잘못된 계정정보입니다."),
    // 제약사항
    CONSTRAINT_VALIDATION_EXCEPTION(400, "CV001", "잘못된 요청 파라미터 입니다."),
    // 회원가입
    DUPLICATE_EMAIL(409, "SU001", "이미 가입된 이메일 입니다."),
    DUPLICATE_PHONE_NUMBER(409, "SU002", "이미 가입된 핸드폰 번호입니다."),
    NOT_VERIFIED_PHONE_NUMBER(404, "SU003", "인증되지 않은 핸드폰 번호입니다."),
    // 유저
    NOT_FOUND_USER(404, "U001", "존재하지 않는 유저입니다."),
    NOT_SIGNED_IN(400, "U002", "로그인 상태가 아닙니다."),
    // 약관
    NOT_FOUND_TERM(404, "T001", "존재하지 않는 약관입니다."),
    NOT_AGREE_ALL_REQUIRED_TERM(400, "T002", "모든 필수 약관에 동의가 필요합니다."),
    // 권한 부족
    PERMISSION_REQUIRED(403, "PM001", "권한이 부족합니다."),
    INVALID_PET_NAME(400, "P001", "유효하지 않은 반려동물 이름입니다."),
    INVALID_PET_INTRODUCTION(400, "P002", "유효하지 않은 반려동물 소개입니다."),
    // 소셜 회원가입
    INVALID_OAUTH2_TEMP_KEY(400, "OSU001", "유효하지 않은 소셜 회원가입 임시 키입니다."),
    // 게시판
    BOARD_NOTFOUND_EXCEPTION(400, "BOARD_NOTFOUND_EXCEPTION", "게시글을 찾지 못했습니다."),
    BOARD_REGISTRATION_EXCEPTION(400, "BOARD_REGISTRATION_EXCEPTION", "게시글 등록에 실패했습니다"),
    BOARD_UPDATE_EXCEPTION(400, "BOARD_UPDATE_EXCEPTION", "게시글 수정에 실패했습니다"),
    BOARD_DELETE_EXCEPTION(400, "BOARD_DELETE_EXCEPTION", "게시글 삭제에 실패했습니다"),
    BOARD_LIKE_FAIL_EXCEPTION(400, "BOARD_LIKE_FAIL_EXCEPTION", "좋아요 추가에 실패했습니다."),
    BOARD_DELETE_LIKE_FAIL_EXCEPTION(400, "BOARD_DELETE_LIKE_FAIL_EXCEPTION", "좋아요 취소에 실패했습니다."),
    BOARD_SEARCH_QUERY_EMPTY_EXCEPTION(400, "BOARD_SEARCH_QUERY_EMPTY_EXCEPTION", "검색어를 입력해주세요"),
    BOOKMARK_REGISTRATION_EXCEPTION(400, "BOOKMARK_REGISTRATION_EXCEPTION", "북마크 등록에 실패했습니다"),
    BOOKMARK_DELETE_EXCEPTION(400, "BOOKMARK_DELETE_EXCEPTION", "북마크 취소에 실패했습니다"),
    // 댓글
    REPLY_NOTFOUND_EXCEPTION(400, "REPLY_NOTFOUND_EXCEPTION", "댓글을 찾지 못했습니다"),
    REPLY_REGISTRATION_EXCEPTION(400, "REPLY_REGISTRATION_EXCEPTION", "댓글 등록에 실패했습니다"),
    REPLY_UPDATE_EXCEPTION(400, "REPLY_UPDATE_EXCEPTION", "댓글 수정에 실패했습니다"),
    // SMS
    OUT_OF_SMS_LIMIT(429, "SM001", "일일 SMS 발송 허용량을 초과하였습니다."),
    INVALID_VERIFICATION_CODE(400, "SM002", "유효하지 않은 인증 코드입니다."),
    // 비밀번호 변경
    NOT_FOUND_CHANGE_PASSWORD_TEMP_KEY(404, "CP001", "존재하지 않는 비밀번호 변경 임시 키입니다."),
    // 채팅방
    IS_NOT_CHATROOM_PARTICIPANT(400, "CR001", "채팅방 참여자가 아닙니다."),
    NOT_ALLOWED_CHATROOM_LEAVE(400, "CR002", "채팅방 매니저는 채팅방을 나갈 수 없습니다."),
    NOT_FOUND_CHATROOM(404, "CR003", "존재하지 않는 채팅방 입니다."),
    NOT_A_CHATROOM_PARTICIPANT(400, "CR004", "채팅방 참여자가 아닙니다."),
    NOT_FOUND_CHATROOM_SCHEDULE(404, "CR005", "존재하지 않는 채팅방 스케줄입니다."),
    NOT_A_CHATROOM_SCHEDULE_PARTICIPANT(400, "CR006", "채팅방 스케줄 참여자가 아닙니다."),
    ALREADY_CHATROOM_PARTICIPANT(409, "CR007", "이미 참여한 채팅방입니다."),
    NOT_FOUND_CHATROOM_DEFAULT_COVER(404, "CR008", "존재하지 않는 채팅방 기본 커버입니다."),
    ALREADY_CHATROOM_SCHEDULE_PARTICIPANT(409, "CR009", "이미 참여한 채팅방 스케줄입니다."),
    ALREADY_CHATROOM_MANAGER(409, "CR010", "이미 채팅방 매니저입니다."),
    CHATROOM_PARTICIPANT_EXIST(400, "CR011", "채팅방 참여자가 존재합니다."),
    // 게시판 이미지
    BOARD_IMG_CANNOT_UPLOAD_EXCEPTION(400, "BOARD_IMG_CANNOT_UPLOAD_EXCEPTION", "이미지 업로드를 실패했습니다"),
    BOARD_IMG_CANNOT_VIEW_EXCEPTION(400, "BOARD_IMG_CANNOT_VIEW_EXCEPTION", "이미지 조회를 실패했습니다"),
    BOARD_IMG_CANNOT_REMOVE_EXCEPTION(400, "BOARD_IMG_CANNOT_REMOVE_EXCEPTION", "이미지 삭제를 실패했습니다"),
    // 반려동물
    INVALID_PET_TYPE(400, "PE001", "유효하지 않은 반려동물 종류입니다."),
    NOT_FOUND_PET(404, "PE002", "존재하지 않는 반려동물 입니다."),
    // 파일 업로드
    FILE_SIZE_LIMIT_EXCEPTION(413, "FU001", "파일 크기 제한을 초과하였습니다.");

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
