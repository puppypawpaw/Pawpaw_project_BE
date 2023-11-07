package kr.co.pawpaw.mysql.report.service.query;

import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.report.domain.BoardReport;
import kr.co.pawpaw.mysql.report.repository.ReportRepository;
import kr.co.pawpaw.mysql.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReportQuery {

    private final ReportRepository reportRepository;

    public Optional<BoardReport> deleteBoardReportByUserAndBoard(User user, Board board){
        return reportRepository.deleteBoardReportByUserAndBoard(user, board);
    }
    public boolean existsByUserAndBoard(User user, Board board){
        return reportRepository.existsByUserAndBoard(user, board);
    }
}
