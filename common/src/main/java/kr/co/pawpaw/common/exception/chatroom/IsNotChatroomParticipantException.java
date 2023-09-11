package kr.co.pawpaw.common.exception.chatroom;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class IsNotChatroomParticipantException extends BusinessException {
    public IsNotChatroomParticipantException() {
        super(ErrorCode.IS_NOT_CHATROOM_PARTICIPANT);
    }
}
