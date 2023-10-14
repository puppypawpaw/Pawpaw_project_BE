package kr.co.pawpaw.common.exception.user;


import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class NotSignedInException extends BusinessException {
    public NotSignedInException() {
        super(ErrorCode.NOT_SIGNED_IN);
    }
}
