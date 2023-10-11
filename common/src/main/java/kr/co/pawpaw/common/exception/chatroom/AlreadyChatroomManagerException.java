package kr.co.pawpaw.common.exception.chatroom;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class AlreadyChatroomManagerException extends BusinessException {
    public AlreadyChatroomManagerException() {
        super(ErrorCode.ALREADY_CHATROOM_MANAGER);
    }
}
