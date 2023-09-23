package kr.co.pawpaw.api.controller.term;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pawpaw.api.service.term.TermService;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.config.annotation.CheckPermission;
import kr.co.pawpaw.api.dto.term.CreateTermRequest;
import kr.co.pawpaw.api.dto.term.TermResponse;
import kr.co.pawpaw.api.dto.term.UpdateTermRequest;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "term")
@RestController
@RequestMapping("/api/term")
@RequiredArgsConstructor
public class TermController {
    private final TermService termService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200")
    })
    @Operation(
        method = "GET",
        summary = "모든 약관 조회",
        description = "모든 약관 조회"
    )
    @GetMapping
    public ResponseEntity<List<TermResponse>> getAllTerms() {
        return ResponseEntity.ok(termService.getAllTerms());
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 약관입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "GET",
        summary = "약관 조회",
        description = "약관 조회"
    )
    @GetMapping("/{id}")
    public ResponseEntity<TermResponse> getTerm(
        @PathVariable final Long id
    ) {
        return ResponseEntity.ok(termService.getTerm(id));
    }

    @CheckPermission
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "403",
            description = "권한이 부족합니다.",
            content = @Content
        )
    })
    @Operation(
        method = "POST",
        summary = "약관 생성(관리자만)",
        description = "약관 생성(관리자만)"
    )
    @PostMapping
    public ResponseEntity<Void> createTerm(
        @AuthenticatedUserId final UserId userId,
        @RequestBody @Valid final CreateTermRequest request
    ) {
        termService.createTerm(request);

        return ResponseEntity.noContent().build();
    }

    @CheckPermission
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "403",
            description = "권한이 부족합니다.",
            content = @Content
        )
    })
    @Operation(
        method = "DELETE",
        summary = "약관 삭제(관리자만)",
        description = "약관 삭제(관리자만)"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerm(
        @AuthenticatedUserId final UserId userId,
        @PathVariable final Long id
    ) {
        termService.deleteTerm(id);

        return ResponseEntity.noContent().build();
    }

    @CheckPermission
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 약관입니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "권한이 부족합니다.",
            content = @Content
        )
    })
    @Operation(
        method = "PATCH",
        summary = "약관 변경(관리자만)",
        description = "약관 변경(관리자만)"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateTerm(
        @AuthenticatedUserId final UserId userId,
        @PathVariable final Long id,
        @RequestBody @Valid final UpdateTermRequest request
    ) {
        termService.updateTerm(id, request);

        return ResponseEntity.noContent().build();
    }
}
