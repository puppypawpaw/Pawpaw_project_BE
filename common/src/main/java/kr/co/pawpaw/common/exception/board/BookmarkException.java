package kr.co.pawpaw.common.exception.board;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class BookmarkException {

    public static class BookmarkRegistrationFailException extends BusinessException {
        public BookmarkRegistrationFailException() {
            super(ErrorCode.BOOKMARK_REGISTRATION_EXCEPTION);
        }
    }

    public static class BookmarkDeleteFailException extends BusinessException {
        public BookmarkDeleteFailException() {
            super(ErrorCode.BOOKMARK_DELETE_EXCEPTION);
        }
    }
}
