package com.puppy.pawpaw_project_be.exception.term;

import com.puppy.pawpaw_project_be.exception.common.BusinessException;
import com.puppy.pawpaw_project_be.exception.common.ErrorCode;

public class NotAgreeAllRequiredTermException extends BusinessException {
    public NotAgreeAllRequiredTermException() {
        super(ErrorCode.NOT_AGREE_ALL_REQUIRED_TERM);
    }
}
