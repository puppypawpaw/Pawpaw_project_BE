package kr.co.pawpaw.common.exception.chatroom;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class NotFoundChatroomException extends BusinessException {
    public NotFoundChatroomException() {
        super(ErrorCode.NOT_FOUND_CHATROOM);
    }
}
