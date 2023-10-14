package kr.co.pawpaw.common.exception.chatroom;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class NotFoundChatroomDefaultCoverException extends BusinessException {
    public NotFoundChatroomDefaultCoverException() {
        super(ErrorCode.NOT_FOUND_CHATROOM_DEFAULT_COVER);
    }
}
