package kr.co.pawpaw.common.exception.term;


import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class NotAgreeAllRequiredTermException extends BusinessException {
    public NotAgreeAllRequiredTermException() {
        super(ErrorCode.NOT_AGREE_ALL_REQUIRED_TERM);
    }
}
