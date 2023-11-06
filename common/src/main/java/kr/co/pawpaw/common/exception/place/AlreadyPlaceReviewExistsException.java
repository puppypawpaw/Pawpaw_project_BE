package kr.co.pawpaw.common.exception.place;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class AlreadyPlaceReviewExistsException extends BusinessException {
    public AlreadyPlaceReviewExistsException() {
        super(ErrorCode.ALREADY_PLACE_REVIEW_EXISTS);
    }
}
