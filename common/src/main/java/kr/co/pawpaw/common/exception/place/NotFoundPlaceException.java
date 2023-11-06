package kr.co.pawpaw.common.exception.place;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class NotFoundPlaceException extends BusinessException {
    public NotFoundPlaceException() {
        super(ErrorCode.NOT_FOUND_PLACE);
    }
}
