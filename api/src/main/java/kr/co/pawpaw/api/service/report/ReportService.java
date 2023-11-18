package kr.co.pawpaw.api.service.report;

import kr.co.pawpaw.common.exception.board.BoardException;
import kr.co.pawpaw.common.exception.report.ReportException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.board.service.query.BoardQuery;
import kr.co.pawpaw.mysql.report.domain.BoardReport;
import kr.co.pawpaw.mysql.report.service.command.ReportCommand;
import kr.co.pawpaw.mysql.report.service.query.ReportQuery;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.service.query.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportQuery reportQuery;
    private final ReportCommand reportCommand;
    private final BoardQuery boardQuery;
    private final UserQuery userQuery;

    @Transactional
    public boolean addReport(Long boardId, UserId userId) {
        Board board = boardQuery.findById(boardId).orElseThrow(BoardException.BoardNotFoundException::new);
        User user = userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);

        if (board.getUser().getUserId().equals(userId)){
            throw new ReportException.CanNotSelfReportException();
        }
        if (!checkReportExist(userId, board)) {
            reportCommand.save(new BoardReport(board, user));
            board.plusReportedCount();
            return true;
        }
        throw new ReportException.ReportCanNotException();
    }

    @Transactional
    public boolean cancelReport(Long boardId, UserId userId) {
        Board board = boardQuery.findById(boardId).orElseThrow(BoardException.BoardNotFoundException::new);
        User user = userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        if (checkReportExist(userId, board)) {
            reportQuery.deleteBoardReportByUserAndBoard(user, board);
            board.minusReportedCount();
            return true;
        }
        throw new ReportException.ReportCancelException();
    }
    public boolean checkReportExist(UserId userId, Board board) {
        return reportQuery.existsByUser_UserIdAndBoard(userId, board);
    }
}
