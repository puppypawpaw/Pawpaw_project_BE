package com.puppy.pawpaw_project_be.exception.user;

import com.puppy.pawpaw_project_be.exception.common.BusinessException;
import com.puppy.pawpaw_project_be.exception.common.ErrorCode;

public class NotSignedInException extends BusinessException {
    public NotSignedInException() {
        super(ErrorCode.NOT_SIGNED_IN);
    }
}
