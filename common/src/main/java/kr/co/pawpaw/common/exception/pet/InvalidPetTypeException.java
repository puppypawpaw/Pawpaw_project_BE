package kr.co.pawpaw.common.exception.pet;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class InvalidPetTypeException extends BusinessException {
    public InvalidPetTypeException() {
        super(ErrorCode.INVALID_PET_TYPE);
    }
}
