package kr.co.pawpaw.common.exception.report;

import kr.co.pawpaw.common.exception.common.BusinessException;
import kr.co.pawpaw.common.exception.common.ErrorCode;

public class ReportException {

    public static class ReportCanNotException extends BusinessException{
        public ReportCanNotException() {
            super(ErrorCode.REPORT_CANNOT_EXCEPTION);
        }
    }

    public static class ReportCancelException extends BusinessException{
        public ReportCancelException() {
            super(ErrorCode.REPORT_CANCEL_EXCEPTION);
        }
    }

    public static class CanNotSelfReportException extends BusinessException{
        public CanNotSelfReportException() {
            super(ErrorCode.REPORT_CANNOT_MYSELF);
        }
    }
}
