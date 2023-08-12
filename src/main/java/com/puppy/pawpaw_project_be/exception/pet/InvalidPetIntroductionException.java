package com.puppy.pawpaw_project_be.exception.pet;

import com.puppy.pawpaw_project_be.exception.common.BusinessException;
import com.puppy.pawpaw_project_be.exception.common.ErrorCode;

public class InvalidPetIntroductionException extends BusinessException {
    public InvalidPetIntroductionException() {
        super(ErrorCode.INVALID_PET_INTRODUCTION);
    }
}
