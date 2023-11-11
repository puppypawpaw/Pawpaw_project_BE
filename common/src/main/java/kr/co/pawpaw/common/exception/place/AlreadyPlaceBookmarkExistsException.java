package kr.co.pawpaw.common.exception.place;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class AlreadyPlaceBookmarkExistsException extends BusinessException {
    public AlreadyPlaceBookmarkExistsException() {
        super(ErrorCode.ALREADY_PLACE_BOOKMARK_EXISTS);
    }
}