package kr.co.pawpaw.api.controller.place;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.config.annotation.CheckPermission;
import kr.co.pawpaw.api.dto.place.CreatePlaceRequest;
import kr.co.pawpaw.api.service.place.PlaceService;
import kr.co.pawpaw.mysql.place.domain.PlaceType;
import kr.co.pawpaw.mysql.place.dto.PlaceResponse;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name= "place")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/place")
public class PlaceController {
    private final PlaceService placeService;

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
}
