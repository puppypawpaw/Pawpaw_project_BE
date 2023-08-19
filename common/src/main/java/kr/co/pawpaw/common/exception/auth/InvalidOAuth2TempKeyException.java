package kr.co.pawpaw.common.exception.auth;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class InvalidOAuth2TempKeyException extends BusinessException {
    public InvalidOAuth2TempKeyException() {
        super(ErrorCode.INVALID_OAUTH2_TEMP_KEY);
    }
}
