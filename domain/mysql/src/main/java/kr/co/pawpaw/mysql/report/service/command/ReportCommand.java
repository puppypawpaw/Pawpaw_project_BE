package kr.co.pawpaw.mysql.report.service.command;

import kr.co.pawpaw.mysql.report.domain.BoardReport;
import kr.co.pawpaw.mysql.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportCommand {

    private final ReportRepository reportRepository;

    public void save(BoardReport boardReport){
        reportRepository.save(boardReport);
    }
}