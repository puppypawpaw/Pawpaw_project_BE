package kr.co.pawpaw.common.exception.common;

public class PermissionRequiredException extends BusinessException {
    public PermissionRequiredException() {
        super(ErrorCode.PERMISSION_REQUIRED);
    }
}
