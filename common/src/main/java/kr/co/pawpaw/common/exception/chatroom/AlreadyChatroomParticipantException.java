package kr.co.pawpaw.common.exception.chatroom;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class AlreadyChatroomParticipantException extends BusinessException {
    public AlreadyChatroomParticipantException() {
        super(ErrorCode.ALREADY_CHATROOM_PARTICIPANT);
    }
}
