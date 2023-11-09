package kr.co.pawpaw.common.exception.place;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class NotFoundPlaceReviewException extends BusinessException {
    public NotFoundPlaceReviewException() {
        super(ErrorCode.NOT_FOUND_PLACE_REVIEW);
    }
}