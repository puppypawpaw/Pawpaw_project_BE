package com.puppy.pawpaw_project_be.exception.term;

import com.puppy.pawpaw_project_be.exception.common.BusinessException;
import com.puppy.pawpaw_project_be.exception.common.ErrorCode;

public class NotFoundTermException extends BusinessException {
    public NotFoundTermException() {
        super(ErrorCode.NOT_FOUND_TERM);
    }
}
