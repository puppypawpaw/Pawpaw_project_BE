package kr.co.pawpaw.common.exception.pet;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class NotFoundPetException extends BusinessException {
    public NotFoundPetException() {
        super(ErrorCode.NOT_FOUND_PET);
    }
}
