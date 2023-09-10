package kr.co.pawpaw.common.exception.chatroom;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class NotAllowedChatroomLeaveException extends BusinessException {
    public NotAllowedChatroomLeaveException() {
        super(ErrorCode.NOT_ALLOWED_CHATROOM_LEAVE);
    }
}
