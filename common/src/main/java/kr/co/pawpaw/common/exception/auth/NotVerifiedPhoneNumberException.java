package kr.co.pawpaw.common.exception.auth;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class NotVerifiedPhoneNumberException extends BusinessException {
    public NotVerifiedPhoneNumberException() {
        super(ErrorCode.NOT_VERIFIED_PHONE_NUMBER);
    }
}
