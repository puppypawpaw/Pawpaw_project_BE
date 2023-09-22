package kr.co.pawpaw.common.exception.chatroom;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class NotAChatroomScheduleParticipantException extends BusinessException {
    public NotAChatroomScheduleParticipantException() {
        super(ErrorCode.NOT_A_CHATROOM_SCHEDULE_PARTICIPANT);
    }
}
