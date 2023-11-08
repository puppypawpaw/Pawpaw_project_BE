package kr.co.pawpaw.api.controller.report;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.service.report.ReportService;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "reports", description = "신고 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "게시글 신고에 실패했습니다")
    })
    @Operation(
            method = "POST",
            summary = "게시글 신고",
            description = "해당하는 게시글을 신고한다."
    )
    @PostMapping
    public ResponseEntity<Boolean> addReport(@RequestParam Long boardId, @AuthenticatedUserId UserId userId) {
        return ResponseEntity.ok(reportService.addReport(boardId, userId));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "게시글 신고에 실패했습니다")
    })
    @Operation(
            method = "DELETE",
            summary = "게시글 신고 취소",
            description = "게시글 신고를 취소한다."
    )
    @DeleteMapping
    public ResponseEntity<Boolean> cancelReport(@RequestParam Long boardId, @AuthenticatedUserId UserId userId) {
        return ResponseEntity.ok(reportService.cancelReport(boardId, userId));
    }
}
