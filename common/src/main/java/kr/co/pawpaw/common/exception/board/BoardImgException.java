package kr.co.pawpaw.common.exception.board;


import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class BoardImgException {

    public static class BoardImgCanNotUploadException extends BusinessException {
        public BoardImgCanNotUploadException() {
            super(ErrorCode.BOARD_IMG_CANNOT_UPLOAD_EXCEPTION);
        }
    }

    public static class BoardImgCanNotViewException extends BusinessException {
        public BoardImgCanNotViewException() {
            super(ErrorCode.BOARD_IMG_CANNOT_VIEW_EXCEPTION);
        }
    }

    public static class BoardImgCanNotRemoveException extends BusinessException {
        public BoardImgCanNotRemoveException() {
            super(ErrorCode.BOARD_IMG_CANNOT_REMOVE_EXCEPTION);
        }
    }
}
