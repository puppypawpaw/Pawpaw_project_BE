package kr.co.pawpaw.api.service.place;

import kr.co.pawpaw.api.dto.place.CreatePlaceRequest;
import kr.co.pawpaw.api.dto.place.CreatePlaceReviewRequest;
import kr.co.pawpaw.api.service.file.FileService;
import kr.co.pawpaw.common.exception.place.AlreadyPlaceBookmarkExistsException;
import kr.co.pawpaw.common.exception.place.NotFoundPlaceException;
import kr.co.pawpaw.common.exception.place.NotFoundPlaceReviewException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.mysql.place.domain.*;
import kr.co.pawpaw.mysql.place.dto.PlaceResponse;
import kr.co.pawpaw.mysql.place.dto.PlaceReviewResponse;
import kr.co.pawpaw.mysql.place.service.command.*;
import kr.co.pawpaw.mysql.place.service.query.PlaceBookmarkQuery;
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

import java.util.ArrayList;
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
    private final PlaceBookmarkCommand placeBookmarkCommand;
    private final PlaceBookmarkQuery placeBookmarkQuery;
    private final PlaceImageUrlCommand placeImageUrlCommand;

    public PlaceResponse getPlace(
        final Long placeId
    ) {
        return placeQuery.findByPlaceIdAsPlaceResponse(placeId)
            .orElseThrow(NotFoundPlaceException::new);
    }

    @Transactional
    public void addBookmarkPlace(
        final UserId userId,
        final Long placeId
    ) {
        Place place = placeQuery.findByPlaceId(placeId)
                .orElseThrow(NotFoundPlaceException::new);

        User user = userQuery.findByUserId(userId)
                .orElseThrow(NotFoundUserException::new);

        if (placeBookmarkQuery.existsByPlaceIdAndUserId(placeId, userId)) {
            throw new AlreadyPlaceBookmarkExistsException();
        }

        placeBookmarkCommand.save(PlaceBookmark.builder()
                .place(place)
                .user(user)
            .build());
    }

    @Transactional
    public void deleteBookmarkPlace(
        final UserId userId,
        final Long placeId
    ) {
        placeBookmarkCommand.deleteByPlaceIdAndUserUserId(placeId, userId);
    }

    public PlaceReviewResponse getMyPlaceReview(
        final UserId userId,
        final Long placeId
    ) {
        return placeReviewQuery.findByPlaceIdAndReviewerUserIdAsPlaceReviewResponse(placeId, userId)
            .orElseThrow(NotFoundPlaceReviewException::new);
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
        PlaceReview placeReview = placeReviewCommand.findByPlaceIdAndReviewerUserId(placeId, userId)
            .orElseThrow(NotFoundPlaceReviewException::new);

        placeReviewCommand.delete(placeReview);

        placeCommand.updatePlaceReviewInfo(
            placeId,
            -1,
            -placeReview.getScore(),
            -booleanToInt(placeReview.isQuiet()),
            -booleanToInt(placeReview.isAccessible()),
            -booleanToInt(placeReview.isSafe()),
            -booleanToInt(placeReview.isScenic()),
            -booleanToInt(placeReview.isClean()),
            -booleanToInt(placeReview.isComfortable())
        );
    }

    @Transactional
    public void createPlaceAll(final List<CreatePlaceRequest> requestList) {
        List<Place> placeList = placeCommand.saveAll(requestList.stream()
            .map(CreatePlaceRequest::toPlace)
            .collect(Collectors.toList()));

        List<PlaceImageUrl> placeImageUrlList = new ArrayList<>();

        for (int i = requestList.size(); i > 0; --i) {
            Place place = placeList.get(i - 1);
            CreatePlaceRequest request = requestList.get(i - 1);

            placeImageUrlList.addAll(request.toPlaceImageUrls(place));
        }

        placeImageUrlCommand.saveAll(placeImageUrlList);
    }

    @Transactional
    public PlaceReview createOrUpdatePlaceReview(
        final Long placeId,
        final UserId userId,
        final CreatePlaceReviewRequest request
    ) {
        User reviewer = userQuery.getReferenceById(userId);
        Place place = placeQuery.findByPlaceId(placeId)
            .orElseThrow(NotFoundPlaceException::new);

        PlaceReview placeReview = placeReviewQuery.findByPlaceIdAndReviewerUserId(placeId, userId)
            .orElse(null);

        if (Objects.isNull(placeReview))
            return createPlaceReview(request, reviewer, place);

        return updatePlaceReview(request, placeReview, placeId);
    }

    private PlaceReview updatePlaceReview(
        final CreatePlaceReviewRequest request,
        final PlaceReview placeReview,
        final Long placeId
    ) {
        placeCommand.updatePlaceReviewInfo(
            placeId,
            0,
            request.getScore() - placeReview.getScore(),
            booleanToInt(request.isQuiet()) - booleanToInt(placeReview.isQuiet()),
            booleanToInt(request.isAccessible()) - booleanToInt(placeReview.isAccessible()),
            booleanToInt(request.isSafe()) - booleanToInt(placeReview.isSafe()),
            booleanToInt(request.isScenic()) - booleanToInt(placeReview.isScenic()),
            booleanToInt(request.isClean()) - booleanToInt(placeReview.isClean()),
            booleanToInt(request.isComfortable()) - booleanToInt(placeReview.isComfortable())
        );

        placeReview.updateReview(
            request.getScore(),
            request.isScenic(),
            request.isQuiet(),
            request.isClean(),
            request.isComfortable(),
            request.isSafe(),
            request.isAccessible(),
            request.getContent()
        );

        return placeReview;
    }

    private PlaceReview createPlaceReview(
        final CreatePlaceReviewRequest request,
        final User reviewer,
        final Place place
    ) {
        PlaceReview placeReview = request.toPlaceReview(place, reviewer);

        placeReviewCommand.save(placeReview);

        placeCommand.updatePlaceReviewInfo(
            place.getId(),
            1,
            placeReview.getScore(),
            booleanToInt(placeReview.isQuiet()),
            booleanToInt(placeReview.isAccessible()),
            booleanToInt(placeReview.isSafe()),
            booleanToInt(placeReview.isScenic()),
            booleanToInt(placeReview.isClean()),
            booleanToInt(placeReview.isComfortable())
        );

        return placeReview;
    }

    private int booleanToInt(final boolean bool) {
        return bool ? 1 : 0;
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
