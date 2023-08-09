package com.puppy.pawpaw_project_be.exception.auth;

import com.puppy.pawpaw_project_be.exception.common.BusinessException;
import com.puppy.pawpaw_project_be.exception.common.ErrorCode;

public class NotEqualPasswordConfirmException extends BusinessException {
    public NotEqualPasswordConfirmException() {
        super(ErrorCode.NOT_EQUAL_PASSWORD_CONFIRM);
    }
}
