package kr.co.pawpaw.common.exception.sms;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class OutOfSmsLimitException extends BusinessException {
    public OutOfSmsLimitException() {
        super(ErrorCode.OUT_OF_SMS_LIMIT);
    }
}
