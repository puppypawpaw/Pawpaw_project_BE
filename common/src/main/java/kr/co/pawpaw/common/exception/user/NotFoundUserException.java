package kr.co.pawpaw.common.exception.user;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class NotFoundUserException extends BusinessException {
    public NotFoundUserException() {
        super(ErrorCode.NOT_FOUND_USER);
    }
}
