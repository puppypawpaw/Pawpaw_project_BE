package kr.co.pawpaw.common.exception.chatroom;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class ChatroomParticipantExistException extends BusinessException {
    public ChatroomParticipantExistException() {
        super(ErrorCode.CHATROOM_PARTICIPANT_EXIST);
    }
}
