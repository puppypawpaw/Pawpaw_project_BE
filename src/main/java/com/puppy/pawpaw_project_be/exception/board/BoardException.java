package com.puppy.pawpaw_project_be.exception.board;

import com.puppy.pawpaw_project_be.exception.common.BusinessException;
import com.puppy.pawpaw_project_be.exception.common.ErrorCode;

public class BoardException{


    public static class BoardNotFoundException extends BusinessException {
        public BoardNotFoundException() {
            super(ErrorCode.BOARD_REGISTRATION_EXCEPTION);
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
}
