package com.puppy.pawpaw_project_be.exception.pet;

import com.puppy.pawpaw_project_be.exception.common.BusinessException;
import com.puppy.pawpaw_project_be.exception.common.ErrorCode;

public class InvalidPetNameException extends BusinessException {
    public InvalidPetNameException() {
        super(ErrorCode.INVALID_PET_NAME);
    }
}
