package kr.co.pawpaw.common.exception.reply;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class ReplyException {

    public static class ReplyNotFoundException extends BusinessException{
        public ReplyNotFoundException() {
            super(ErrorCode.REPLY_NOTFOUND_EXCEPTION);
        }
    }

    public static class ReplyRegisterException extends BusinessException {
        public ReplyRegisterException() {
            super(ErrorCode.REPLY_REGISTRATION_EXCEPTION);
        }
    }

    public static class ReplyUpdaterException extends BusinessException {
        public ReplyUpdaterException() {
            super(ErrorCode.REPLY_UPDATE_EXCEPTION);
        }
    }
}
