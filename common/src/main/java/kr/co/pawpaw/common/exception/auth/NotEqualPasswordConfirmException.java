package kr.co.pawpaw.common.exception.auth;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class NotEqualPasswordConfirmException extends BusinessException {
    public NotEqualPasswordConfirmException() {
        super(ErrorCode.NOT_EQUAL_PASSWORD_CONFIRM);
    }
}
