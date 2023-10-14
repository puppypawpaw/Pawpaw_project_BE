package kr.co.pawpaw.common.exception.pet;


import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class InvalidPetNameException extends BusinessException {
    public InvalidPetNameException() {
        super(ErrorCode.INVALID_PET_NAME);
    }
}
