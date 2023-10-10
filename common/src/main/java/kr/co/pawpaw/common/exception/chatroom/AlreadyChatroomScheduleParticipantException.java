package kr.co.pawpaw.common.exception.chatroom;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class AlreadyChatroomScheduleParticipantException extends BusinessException {
    public AlreadyChatroomScheduleParticipantException() {
        super(ErrorCode.ALREADY_CHATROOM_SCHEDULE_PARTICIPANT);
    }
}