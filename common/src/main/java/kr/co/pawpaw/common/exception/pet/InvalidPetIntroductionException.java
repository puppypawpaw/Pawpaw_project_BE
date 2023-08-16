package kr.co.pawpaw.common.exception.pet;


import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class InvalidPetIntroductionException extends BusinessException {
    public InvalidPetIntroductionException() {
        super(ErrorCode.INVALID_PET_INTRODUCTION);
    }
}
