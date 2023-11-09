package kr.co.pawpaw.api.service.place;

import kr.co.pawpaw.api.dto.place.CreatePlaceRequest;
import kr.co.pawpaw.api.dto.place.CreatePlaceReviewRequest;
import kr.co.pawpaw.api.service.file.FileService;
import kr.co.pawpaw.common.exception.place.AlreadyPlaceReviewExistsException;
import kr.co.pawpaw.common.exception.place.NotFoundPlaceException;
import kr.co.pawpaw.common.exception.place.NotFoundPlaceReviewException;
import kr.co.pawpaw.mysql.place.domain.Place;
import kr.co.pawpaw.mysql.place.domain.PlaceReview;
import kr.co.pawpaw.mysql.place.domain.PlaceReviewImage;
import kr.co.pawpaw.mysql.place.domain.PlaceType;
import kr.co.pawpaw.mysql.place.dto.PlaceResponse;
import kr.co.pawpaw.mysql.place.dto.PlaceReviewResponse;
import kr.co.pawpaw.mysql.place.service.command.PlaceCommand;
import kr.co.pawpaw.mysql.place.service.command.PlaceReviewCommand;
import kr.co.pawpaw.mysql.place.service.command.PlaceReviewImageCommand;
import kr.co.pawpaw.mysql.place.service.query.PlaceQuery;
import kr.co.pawpaw.mysql.place.service.query.PlaceReviewQuery;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.service.query.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
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
    private final PlaceReviewQuery placeReviewQuery;
    private final PlaceReviewImageCommand placeReviewImageCommand;
    private final FileService fileService;

    public PlaceReviewResponse getMyPlaceReview(
        final UserId userId,
        final Long placeId
    ) {
        return placeReviewQuery.findByPlaceIdAndReviewerUserId(placeId, userId);
    }

    public Slice<PlaceReviewResponse> getPlaceReviewList(
        final UserId userId,
        final Long placeId,
        final Long beforeReviewId,
        final int size
    ) {
        if (!placeQuery.existsById(placeId)) {
            throw new NotFoundPlaceException();
        }

        return placeReviewQuery.findByPlaceIdAndIdBefore(userId, placeId, beforeReviewId, size);
    }

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
    public void deleteMyPlaceReview(final UserId userId, final Long placeId) {
        placeReviewCommand.deleteByPlaceIdAndReviewerUserId(placeId, userId);
    }

    @Transactional
    public void createPlaceAll(final List<CreatePlaceRequest> requestList) {
        placeCommand.saveAll(requestList.stream()
            .map(CreatePlaceRequest::toPlace)
            .collect(Collectors.toList()));
    }

    @Transactional
    public PlaceReview createPlaceReview(
        final Long placeId,
        final UserId userId,
        final CreatePlaceReviewRequest request
    ) {
        User reviewer = userQuery.getReferenceById(userId);
        Place place = placeQuery.findByPlaceId(placeId)
            .orElseThrow(NotFoundPlaceException::new);

        if (placeReviewQuery.existsByPlaceIdAndReviewerUserId(placeId, userId)) {
            throw new AlreadyPlaceReviewExistsException();
        }

        PlaceReview placeReview = request.toPlaceReview(place, reviewer);

        placeReviewCommand.save(placeReview);
        placeCommand.updatePlaceReviewInfo(place, placeReview);

        return placeReview;
    }

    @Transactional
    public void createPlaceReviewImageList(
        final UserId userId,
        final Long placeId,
        final Long placeReviewId,
        final List<MultipartFile> placeReviewImageMultipartFileList
    ) {
        PlaceReview placeReview = placeReviewQuery.findByPlaceIdAndId(placeId, placeReviewId, userId)
                .orElseThrow(NotFoundPlaceReviewException::new);

        placeReview.addReviewImageList(createPlaceReviewImageList(userId, placeReviewImageMultipartFileList));

        placeReviewImageCommand.saveAll(placeReview.getPlaceReviewImageList());
    }

    @Transactional
    public void deletePlaceReviewImage(
        final UserId userId,
        final Long placeId,
        final Long placeReviewId,
        final List<Long> placeReviewImageIdList
    ) {
        if (!placeReviewQuery.existsByPlaceIdAndReviewerUserId(placeId, userId)) {
            throw new NotFoundPlaceReviewException();
        }

        placeReviewImageCommand.deleteByPlaceReviewIdAndPlaceReviewImageIdIn(placeReviewId, placeReviewImageIdList);
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
