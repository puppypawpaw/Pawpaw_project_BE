package com.puppy.pawpaw_project_be.exception.user;

import com.puppy.pawpaw_project_be.exception.common.BusinessException;
import com.puppy.pawpaw_project_be.exception.common.ErrorCode;

public class DuplicateIdException extends BusinessException {
    public DuplicateIdException() {
        super(ErrorCode.DUPLICATE_ID);
    }
}
