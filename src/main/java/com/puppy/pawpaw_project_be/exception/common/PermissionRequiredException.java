package com.puppy.pawpaw_project_be.exception.common;

public class PermissionRequiredException extends BusinessException {
    public PermissionRequiredException() {
        super(ErrorCode.PERMISSION_REQUIRED);
    }
}
