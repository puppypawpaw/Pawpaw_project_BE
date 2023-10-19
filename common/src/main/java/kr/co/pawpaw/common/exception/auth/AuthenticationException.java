package kr.co.pawpaw.common.exception.auth;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class AuthenticationException extends BusinessException {
    public AuthenticationException() {
        super(ErrorCode.HANDLE_AUTHENTICATION_EXCEPTION);
    }
}
