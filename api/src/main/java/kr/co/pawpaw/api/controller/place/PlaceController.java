package kr.co.pawpaw.api.controller.place;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.config.annotation.CheckPermission;
import kr.co.pawpaw.api.dto.place.CreatePlaceRequest;
import kr.co.pawpaw.api.dto.place.CreatePlaceReviewRequest;
import kr.co.pawpaw.api.service.place.PlaceService;
import kr.co.pawpaw.mysql.place.domain.PlaceType;
import kr.co.pawpaw.mysql.place.dto.PlaceResponse;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name= "place")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/place")
public class PlaceController {
    private final PlaceService placeService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
    })
    @Operation(
        method = "GET",
        summary = "장소 검색",
        description = "장소 검색"
    )
    @GetMapping("/search")
    public ResponseEntity<List<PlaceResponse>> queryPlace(
        @AuthenticatedUserId final UserId userId,
        @RequestParam(required = false) final String query,
        @RequestParam(required = false) final PlaceType placeType,
        @RequestParam(required = false) final Double latMin,
        @RequestParam(required = false) final Double latMax,
        @RequestParam(required = false) final Double longMin,
        @RequestParam(required = false) final Double longMax
    ) {
        return ResponseEntity.ok(placeService.queryPlace(userId, placeType, query, latMin, latMax, longMin, longMax));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204")
    })
    @Operation(
        method = "POST",
        summary = "장소 생성(여러개), 관리자만 됨",
        description = "장소 생성(여러개), 관리자만 됨"
    )
    @CheckPermission
    @PostMapping
    public ResponseEntity<Void> createPlaceAll(
        @AuthenticatedUserId final UserId userId,
        @RequestBody final List<CreatePlaceRequest> requestList
    ) {
        placeService.createPlaceAll(requestList);

        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "404",
            description= "존재하지 않는 장소입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "POST",
        summary = "장소 리뷰 생성",
        description = "장소 리뷰 생성"
    )
    @PostMapping(value = "/{placeId}/review", consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<Void> createPlaceReview(
        @AuthenticatedUserId final UserId userId,
        @PathVariable final Long placeId,
        @RequestPart(required = false) final List<MultipartFile> images,
        @RequestPart final CreatePlaceReviewRequest body
    ) {
        placeService.createPlaceReview(placeId, userId, images, body);

        return ResponseEntity.noContent().build();
    }
}
