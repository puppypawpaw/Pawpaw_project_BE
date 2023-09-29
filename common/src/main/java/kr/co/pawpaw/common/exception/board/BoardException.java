package kr.co.pawpaw.common.exception.board;


import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class BoardException{


    public static class BoardNotFoundException extends BusinessException {
        public BoardNotFoundException() {
            super(ErrorCode.BOARD_NOTFOUND_EXCEPTION);
        }
    }

    public static class BoardRegisterException extends BusinessException {
        public BoardRegisterException() {
            super(ErrorCode.BOARD_REGISTRATION_EXCEPTION);
        }
    }

    public static class BoardUpdateException extends BusinessException{
        public BoardUpdateException() {
            super(ErrorCode.BOARD_UPDATE_EXCEPTION);
        }
    }

    public static class BoardDeleteException extends BusinessException{
        public BoardDeleteException() {
            super(ErrorCode.BOARD_DELETE_EXCEPTION);
        }
    }

    public static class BoardSearchQueryException extends BusinessException{
        public BoardSearchQueryException() {
            super(ErrorCode.BOARD_SEARCH_QUERY_EMPTY_EXCEPTION);
        }
    }
}
