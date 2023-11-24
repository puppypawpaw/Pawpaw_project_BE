package kr.co.pawpaw.mysql.report.repository;

import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.report.domain.BoardReport;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<BoardReport, Long> {

    Optional<BoardReport> deleteBoardReportByUserAndBoard(User user, Board board);

    boolean existsByUser_UserIdAndBoard(UserId userId, Board board);
}
