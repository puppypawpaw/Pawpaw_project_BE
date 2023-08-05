package com.puppy.pawpaw_project_be.exception.user;

import com.puppy.pawpaw_project_be.exception.common.BusinessException;
import com.puppy.pawpaw_project_be.exception.common.ErrorCode;

public class NotFoundUserException extends BusinessException {
    public NotFoundUserException() {
        super(ErrorCode.NOT_FOUND_USER);
    }
}
