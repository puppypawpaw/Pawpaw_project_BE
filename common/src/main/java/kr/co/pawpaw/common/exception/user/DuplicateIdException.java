package kr.co.pawpaw.common.exception.user;


import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class DuplicateIdException extends BusinessException {
    public DuplicateIdException() {
        super(ErrorCode.DUPLICATE_ID);
    }
}
