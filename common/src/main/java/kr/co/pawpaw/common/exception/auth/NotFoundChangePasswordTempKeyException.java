package kr.co.pawpaw.common.exception.auth;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class NotFoundChangePasswordTempKeyException extends BusinessException {
    public NotFoundChangePasswordTempKeyException() {
        super(ErrorCode.NOT_FOUND_CHANGE_PASSWORD_TEMP_KEY);
    }
}