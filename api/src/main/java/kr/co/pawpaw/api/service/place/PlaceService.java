package kr.co.pawpaw.api.service.place;

import kr.co.pawpaw.api.dto.place.CreatePlaceRequest;
import kr.co.pawpaw.api.dto.place.CreatePlaceReviewRequest;
import kr.co.pawpaw.api.service.file.FileService;
import kr.co.pawpaw.common.exception.place.NotFoundPlaceException;
import kr.co.pawpaw.mysql.place.domain.Place;
import kr.co.pawpaw.mysql.place.domain.PlaceReview;
import kr.co.pawpaw.mysql.place.domain.PlaceReviewImage;
import kr.co.pawpaw.mysql.place.domain.PlaceType;
import kr.co.pawpaw.mysql.place.dto.PlaceResponse;
import kr.co.pawpaw.mysql.place.service.command.PlaceCommand;
import kr.co.pawpaw.mysql.place.service.command.PlaceReviewCommand;
import kr.co.pawpaw.mysql.place.service.query.PlaceQuery;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.service.query.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceQuery placeQuery;
    private final UserQuery userQuery;
    private final PlaceCommand placeCommand;
    private final PlaceReviewCommand placeReviewCommand;
    private final FileService fileService;

    public List<PlaceResponse> queryPlace(
        final UserId userId,
        final PlaceType placeType,
        final String query,
        final Double latitudeMin,
        final Double latitudeMax,
        final Double longitudeMin,
        final Double longitudeMax
    ) {
        return placeQuery.findByQueryAndPlaceTypeAndPositionRange(
            query,
            placeType,
            latitudeMin,
            latitudeMax,
            longitudeMin,
            longitudeMax,
            userId
        );
    }

    @Transactional
    public void createPlaceAll(final List<CreatePlaceRequest> requestList) {
        placeCommand.saveAll(requestList.stream()
            .map(CreatePlaceRequest::toPlace)
            .collect(Collectors.toList()));
    }

    @Transactional
    public void createPlaceReview(
        final Long placeId,
        final UserId userId,
        final List<MultipartFile> placeReviewImageMultipartFileList,
        final CreatePlaceReviewRequest request
    ) {
        User reviewer = userQuery.getReferenceById(userId);
        Place place = placeQuery.findByPlaceId(placeId)
            .orElseThrow(NotFoundPlaceException::new);

        PlaceReview placeReview = request.toPlaceReview(place, reviewer);
        List<PlaceReviewImage> placeReviewImageList = createPlaceReviewImageList(userId, placeReviewImageMultipartFileList);

        placeReview.addReviewImageList(placeReviewImageList);
        placeReviewCommand.save(placeReview);
        placeCommand.updatePlaceReviewInfo(place, placeReview);
    }

    private List<PlaceReviewImage> createPlaceReviewImageList(
        final UserId userId,
        final List<MultipartFile> placeReviewImageMultipartFileList
    ) {
        if (Objects.isNull(placeReviewImageMultipartFileList)) return Collections.emptyList();

        return placeReviewImageMultipartFileList.stream()
            .map(multipartFile -> PlaceReviewImage.builder()
                .image(fileService.saveFileByMultipartFile(multipartFile, userId))
                .build())
            .collect(Collectors.toList());
    }
}
