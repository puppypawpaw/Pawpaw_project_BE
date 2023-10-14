package kr.co.pawpaw.common.exception.chatroom;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class NotAChatroomParticipantException extends BusinessException {
    public NotAChatroomParticipantException() {
        super(ErrorCode.NOT_A_CHATROOM_PARTICIPANT);
    }
}
