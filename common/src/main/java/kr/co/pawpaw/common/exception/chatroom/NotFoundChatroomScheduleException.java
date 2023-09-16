package kr.co.pawpaw.common.exception.chatroom;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class NotFoundChatroomScheduleException extends BusinessException {
    public NotFoundChatroomScheduleException() {
        super(ErrorCode.NOT_FOUND_CHATROOM_SCHEDULE);
    }
}
