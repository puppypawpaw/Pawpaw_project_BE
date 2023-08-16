package kr.co.pawpaw.common.exception.term;


import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class NotFoundTermException extends BusinessException {
    public NotFoundTermException() {
        super(ErrorCode.NOT_FOUND_TERM);
    }
}
