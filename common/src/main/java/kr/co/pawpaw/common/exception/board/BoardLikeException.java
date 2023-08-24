package kr.co.pawpaw.common.exception.board;


import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class BoardLikeException {

    public static class BoardLikeFailException extends BusinessException {
        public BoardLikeFailException() {
            super(ErrorCode.BOARD_LIKE_FAIL_EXCEPTION);
        }
    }

    public static class BoardDeleteLikeFailException extends BusinessException {
        public BoardDeleteLikeFailException() {
            super(ErrorCode.BOARD_DELETE_LIKE_FAIL_EXCEPTION);
        }
    }

}
