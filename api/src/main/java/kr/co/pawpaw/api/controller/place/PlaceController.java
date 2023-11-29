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
import kr.co.pawpaw.api.dto.place.CreatePlaceReviewResponse;
import kr.co.pawpaw.api.service.place.PlaceService;
import kr.co.pawpaw.mysql.place.domain.PlaceReview;
import kr.co.pawpaw.mysql.place.domain.PlaceType;
import kr.co.pawpaw.mysql.place.dto.PlaceResponse;
import kr.co.pawpaw.mysql.place.dto.PlaceReviewResponse;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
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
        @ApiResponse(responseCode = "200"),
    })
    @Operation(
        method = "GET",
        summary = "장소 조회",
        description = "단일 장소 조회"
    )
    @GetMapping("/{placeId}")
    public ResponseEntity<PlaceResponse> getPlace(
        @AuthenticatedUserId final UserId userId,
        @PathVariable final Long placeId
    ) {
        return ResponseEntity.ok(placeService.getPlace(placeId));
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
        method = "PUT",
        summary = "장소 리뷰 생성 및 수정",
        description = "장소 리뷰 및 수정"
    )
    @PutMapping(value = "/{placeId}/review")
    public ResponseEntity<CreatePlaceReviewResponse> createPlaceReview(
        @AuthenticatedUserId final UserId userId,
        @PathVariable final Long placeId,
        @RequestBody final CreatePlaceReviewRequest body
    ) {
        PlaceReview placeReview = placeService.createOrUpdatePlaceReview(placeId, userId, body);

        return ResponseEntity.ok(CreatePlaceReviewResponse.of(placeReview));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 장소 리뷰입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "POST",
        summary = "장소 리뷰 이미지 생성",
        description = "장소 리뷰 이미지 생성"
    )
    @PostMapping(value = "/{placeId}/review/{placeReviewId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createPlaceReviewImage(
        @AuthenticatedUserId final UserId userId,
        @PathVariable final Long placeId,
        @PathVariable final Long placeReviewId,
        @RequestPart(required = false) final List<MultipartFile> images
    ) {
        placeService.createPlaceReviewImageList(userId, placeId, placeReviewId, images);

        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 장소 리뷰입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "DELETE",
        summary = "장소 리뷰 이미지 삭제",
        description = "장소 리뷰 이미지 삭제"
    )
    @DeleteMapping(value = "/{placeId}/review/{placeReviewId}/image")
    public ResponseEntity<Void> deletePlaceReviewImage(
        @AuthenticatedUserId final UserId userId,
        @PathVariable final Long placeId,
        @PathVariable final Long placeReviewId,
        @RequestParam final List<Long> placeReviewImageIdList
    ) {
        placeService.deletePlaceReviewImage(userId, placeId, placeReviewId, placeReviewImageIdList);

        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "404",
            description= "존재하지 않는 장소입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "GET",
        summary = "장소 리뷰 조회",
        description = "장소 리뷰 조회(무한스크롤). 본인 리뷰는 조회 안됨. 내 장소 리뷰 조회 기능 쓰셈"
    )
    @GetMapping("/{placeId}/review")
    public ResponseEntity<Slice<PlaceReviewResponse>> getPlaceReviewList(
        @AuthenticatedUserId final UserId userId,
        @PathVariable final Long placeId,
        @RequestParam(name = "beforeReviewId", required = false) final Long beforeReviewId,
        @RequestParam(name = "size", defaultValue = "10") final int size
    ) {
        return ResponseEntity.ok(placeService.getPlaceReviewList(userId, placeId, beforeReviewId, size));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 장소입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "GET",
        summary = "내 장소 리뷰 조회",
        description = "내가 작성한 장소 리뷰 조회"
    )
    @GetMapping("/{placeId}/myReview")
    public ResponseEntity<PlaceReviewResponse> getMyPlaceReview(
        @AuthenticatedUserId final UserId userId,
        @PathVariable final Long placeId
    ) {
        return ResponseEntity.ok(placeService.getMyPlaceReview(userId, placeId));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
    })
    @Operation(
        method = "GET",
        summary = "내 장소 리뷰 삭제",
        description = "내가 작성한 장소 리뷰 삭제"
    )
    @DeleteMapping("/{placeId}/myReview")
    public ResponseEntity<Void> deleteMyPlaceReview(
        @AuthenticatedUserId final UserId userId,
        @PathVariable final Long placeId
    ) {
        placeService.deleteMyPlaceReview(userId, placeId);

        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 장소입니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 유저입니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "이미 북마크한 장소입니다.",
            content = @Content
        ),
    })
    @Operation(
        method = "POST",
        summary = "장소 즐겨찾기 추가",
        description = "장소 즐겨찾기 추가"
    )
    @PostMapping("/{placeId}/bookmarks")
    public ResponseEntity<Void> addBookmarkPlace(
        @AuthenticatedUserId final UserId userId,
        @PathVariable final Long placeId
    ) {
        placeService.addBookmarkPlace(userId, placeId);

        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204")
    })
    @Operation(
        method = "DELETE",
        summary = "장소 즐겨찾기 삭제",
        description = "장소 즐겨찾기 삭제"
    )
    @DeleteMapping("/{placeId}/bookmarks")
    public ResponseEntity<Void> bookmarkPlace(
        @AuthenticatedUserId final UserId userId,
        @PathVariable final Long placeId
    ) {
        placeService.deleteBookmarkPlace(userId, placeId);

        return ResponseEntity.noContent().build();
    }
}
