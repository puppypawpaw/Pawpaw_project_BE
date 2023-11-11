package kr.co.pawpaw.api.service.place;

import kr.co.pawpaw.api.dto.place.CreatePlaceReviewRequest;
import kr.co.pawpaw.api.service.file.FileService;
import kr.co.pawpaw.common.exception.place.AlreadyPlaceBookmarkExistsException;
import kr.co.pawpaw.common.exception.place.NotFoundPlaceException;
import kr.co.pawpaw.common.exception.place.NotFoundPlaceReviewException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.mysql.place.domain.Place;
import kr.co.pawpaw.mysql.place.domain.PlaceReview;
import kr.co.pawpaw.mysql.place.service.command.PlaceCommand;
import kr.co.pawpaw.mysql.place.service.command.PlaceReviewCommand;
import kr.co.pawpaw.mysql.place.service.command.PlaceReviewImageCommand;
import kr.co.pawpaw.mysql.place.service.query.PlaceBookmarkQuery;
import kr.co.pawpaw.mysql.place.service.query.PlaceQuery;
import kr.co.pawpaw.mysql.place.service.query.PlaceReviewQuery;
import kr.co.pawpaw.mysql.storage.domain.File;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.service.query.UserQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PlaceService 의")
class PlaceServiceTest {
    @Mock
    private PlaceQuery placeQuery;
    @Mock
    private UserQuery userQuery;
    @Mock
    private PlaceCommand placeCommand;
    @Mock
    private PlaceReviewImageCommand placeReviewImageCommand;
    @Mock
    private PlaceReviewCommand placeReviewCommand;
    @Mock
    private FileService fileService;
    @Mock
    private MultipartFile multipartFile;
    @Mock
    private PlaceReviewQuery placeReviewQuery;
    @Mock
    private PlaceBookmarkQuery placeBookmarkQuery;

    @InjectMocks
    private PlaceService placeService;

    @Nested
    @DisplayName("addBookmarkPlace 메서드는")
    class AddBookmarkPlace {
        Long placeId = 1L;
        Place place = Place.builder().build();
        User user = User.builder().build();

        @Test
        @DisplayName("존재하지 않는 장소면 예외가 발생한다.")
        void NotFoundPlaceException() {
            //given
            when(placeQuery.findByPlaceId(placeId))
                .thenReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> placeService.addBookmarkPlace(user.getUserId(), placeId))
                .isInstanceOf(NotFoundPlaceException.class);
        }

        @Test
        @DisplayName("존재하지 않는 유저면 예외가 발생한다.")
        void NotFoundUserException() {
            //given
            when(placeQuery.findByPlaceId(placeId))
                .thenReturn(Optional.of(place));
            when(userQuery.findByUserId(user.getUserId()))
                .thenReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> placeService.addBookmarkPlace(user.getUserId(), placeId))
                .isInstanceOf(NotFoundUserException.class);
        }

        @Test
        @DisplayName("이미 북마크를 생성한 장소면 예외가 발생한다.")
        void AlreadyExistsBookmarkException() {
            //given
            when(placeQuery.findByPlaceId(placeId))
                .thenReturn(Optional.of(place));
            when(userQuery.findByUserId(user.getUserId()))
                .thenReturn(Optional.of(user));
            when(placeBookmarkQuery.existsByPlaceIdAndUserId(placeId, user.getUserId())).thenReturn(true);

            //then
            assertThatThrownBy(() -> placeService.addBookmarkPlace(user.getUserId(), placeId))
                .isInstanceOf(AlreadyPlaceBookmarkExistsException.class);
        }

    }

    @Nested
    @DisplayName("createPlaceReview 메서드는")
    class CreatePlaceReview {
        Long placeId = 1L;
        Place place = Place.builder().build();
        User user = User.builder().build();
        CreatePlaceReviewRequest request = CreatePlaceReviewRequest.builder()
            .safe(true)
            .score(5L)
            .content("안전해요")
            .build();

        PlaceReview placeReview = PlaceReview.builder()
            .reviewer(user)
            .place(place)
            .content("ㅎㅇㅎㅇ")
            .isAccessible(true)
            .score(3L)
            .build();

        @BeforeEach
        void setup() throws NoSuchFieldException, IllegalAccessException {
            Field idField = Place.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(place, placeId);
        }

        @Test
        @DisplayName("존재하지 않는 장소의 리뷰를 생성하면 예외가 발생한다.")
        void notFoundPlaceException() {
            //given
            when(userQuery.getReferenceById(user.getUserId())).thenReturn(user);
            when(placeQuery.findByPlaceId(placeId)).thenReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> placeService.createOrUpdatePlaceReview(placeId, user.getUserId(), request))
                .isInstanceOf(NotFoundPlaceException.class);

        }

        @Test
        @DisplayName("리뷰를 작성하지 않은 장소이면 새로운 리뷰를 생성한다.")
        void ifNotExistsReviewThenCreate() {
            //given
            PlaceReview resultExpected = request.toPlaceReview(place, user);

            when(userQuery.getReferenceById(user.getUserId())).thenReturn(user);
            when(placeQuery.findByPlaceId(place.getId())).thenReturn(Optional.of(place));
            when(placeReviewQuery.findByPlaceIdAndReviewerUserId(place.getId(), user.getUserId())).thenReturn(Optional.empty());

            //when
            PlaceReview result = placeService.createOrUpdatePlaceReview(placeId, user.getUserId(), request);

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(resultExpected);
        }

        @Test
        @DisplayName("리뷰를 작성하지 않은 장소이면 리뷰 정보에 리뷰 생성 요청의 각 값들을 더한다.")
        void ifNotExistsReviewThenAddReviewInfo() {
            //given
            long cntExpected = 1;
            long scoreExpected = request.getScore();
            long isQuietExpected = request.isQuiet() ? 1 : 0;
            long isAccessibleExpected = request.isAccessible() ? 1 : 0;
            long isSafeExpected = request.isSafe() ? 1 : 0;
            long isScenicExpected = request.isScenic() ? 1 : 0;
            long isCleanExpected = request.isClean() ? 1 : 0;
            long isComfortableExpected = request.isComfortable() ? 1 : 0;

            when(userQuery.getReferenceById(user.getUserId())).thenReturn(user);
            when(placeQuery.findByPlaceId(place.getId())).thenReturn(Optional.of(place));
            when(placeReviewQuery.findByPlaceIdAndReviewerUserId(place.getId(), user.getUserId())).thenReturn(Optional.empty());

            //when
            placeService.createOrUpdatePlaceReview(placeId, user.getUserId(), request);

            //then
            ArgumentCaptor<Long> cntCaptor = ArgumentCaptor.forClass(Long.class);
            ArgumentCaptor<Long> scoreCaptor = ArgumentCaptor.forClass(Long.class);
            ArgumentCaptor<Long> isQuietCaptor = ArgumentCaptor.forClass(Long.class);
            ArgumentCaptor<Long> isAccessibleCaptor = ArgumentCaptor.forClass(Long.class);
            ArgumentCaptor<Long> isSafeCaptor = ArgumentCaptor.forClass(Long.class);
            ArgumentCaptor<Long> isScenicCaptor = ArgumentCaptor.forClass(Long.class);
            ArgumentCaptor<Long> isCleanCaptor = ArgumentCaptor.forClass(Long.class);
            ArgumentCaptor<Long> isComfortableCaptor = ArgumentCaptor.forClass(Long.class);

            verify(placeCommand).updatePlaceReviewInfo(
                eq(place.getId()),
                cntCaptor.capture(),
                scoreCaptor.capture(),
                isQuietCaptor.capture(),
                isAccessibleCaptor.capture(),
                isSafeCaptor.capture(),
                isScenicCaptor.capture(),
                isCleanCaptor.capture(),
                isComfortableCaptor.capture()
            );

            assertThat(cntCaptor.getValue()).isEqualTo(cntExpected);
            assertThat(scoreCaptor.getValue()).isEqualTo(scoreExpected);
            assertThat(isQuietCaptor.getValue()).isEqualTo(isQuietExpected);
            assertThat(isAccessibleCaptor.getValue()).isEqualTo(isAccessibleExpected);
            assertThat(isSafeCaptor.getValue()).isEqualTo(isSafeExpected);
            assertThat(isScenicCaptor.getValue()).isEqualTo(isScenicExpected);
            assertThat(isCleanCaptor.getValue()).isEqualTo(isCleanExpected);
            assertThat(isComfortableCaptor.getValue()).isEqualTo(isComfortableExpected);
        }

        @Test
        @DisplayName("이미 리뷰를 작성한 장소이면 새로운 요청값으로 업데이트된다.")
        void ifAlreadyExistsReviewThenUpdate() {
            //given
            PlaceReview resultExpected = request.toPlaceReview(place, user);

            when(userQuery.getReferenceById(user.getUserId())).thenReturn(user);
            when(placeQuery.findByPlaceId(place.getId())).thenReturn(Optional.of(place));
            when(placeReviewQuery.findByPlaceIdAndReviewerUserId(place.getId(), user.getUserId())).thenReturn(Optional.of(placeReview));

            //when
            PlaceReview result = placeService.createOrUpdatePlaceReview(placeId, user.getUserId(), request);

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(resultExpected);
        }

        @Test
        @DisplayName("리뷰를 작성한 장소이면 리뷰 정보에 리뷰 생성 요청의 각 값들을 더하고 기존 리뷰의 각 값들을 뺀다.")
        void ifAlreadyExistsReviewThenAddReviewInfoOfRequestAndSubReviewInfoOfOldReview() {
            //given
            long cntExpected = 0;
            long scoreExpected = request.getScore() - placeReview.getScore();
            long isQuietExpected = (request.isQuiet() ? 1 : 0) - (placeReview.isQuiet() ? 1 : 0);
            long isAccessibleExpected = (request.isAccessible() ? 1 : 0) - (placeReview.isAccessible() ? 1 : 0);
            long isSafeExpected = (request.isSafe() ? 1 : 0) - (placeReview.isSafe() ? 1 : 0);
            long isScenicExpected = (request.isScenic() ? 1 : 0) - (placeReview.isScenic() ? 1 : 0);
            long isCleanExpected = (request.isClean() ? 1 : 0) - (placeReview.isClean() ? 1 : 0);
            long isComfortableExpected = (request.isComfortable() ? 1 : 0) - (placeReview.isComfortable() ? 1 : 0);

            when(userQuery.getReferenceById(user.getUserId())).thenReturn(user);
            when(placeQuery.findByPlaceId(place.getId())).thenReturn(Optional.of(place));
            when(placeReviewQuery.findByPlaceIdAndReviewerUserId(place.getId(), user.getUserId())).thenReturn(Optional.of(placeReview));

            //when
            placeService.createOrUpdatePlaceReview(placeId, user.getUserId(), request);

            //then
            ArgumentCaptor<Long> cntCaptor = ArgumentCaptor.forClass(Long.class);
            ArgumentCaptor<Long> scoreCaptor = ArgumentCaptor.forClass(Long.class);
            ArgumentCaptor<Long> isQuietCaptor = ArgumentCaptor.forClass(Long.class);
            ArgumentCaptor<Long> isAccessibleCaptor = ArgumentCaptor.forClass(Long.class);
            ArgumentCaptor<Long> isSafeCaptor = ArgumentCaptor.forClass(Long.class);
            ArgumentCaptor<Long> isScenicCaptor = ArgumentCaptor.forClass(Long.class);
            ArgumentCaptor<Long> isCleanCaptor = ArgumentCaptor.forClass(Long.class);
            ArgumentCaptor<Long> isComfortableCaptor = ArgumentCaptor.forClass(Long.class);

            verify(placeCommand).updatePlaceReviewInfo(
                eq(place.getId()),
                cntCaptor.capture(),
                scoreCaptor.capture(),
                isQuietCaptor.capture(),
                isAccessibleCaptor.capture(),
                isSafeCaptor.capture(),
                isScenicCaptor.capture(),
                isCleanCaptor.capture(),
                isComfortableCaptor.capture()
            );

            assertThat(cntCaptor.getValue()).isEqualTo(cntExpected);
            assertThat(scoreCaptor.getValue()).isEqualTo(scoreExpected);
            assertThat(isQuietCaptor.getValue()).isEqualTo(isQuietExpected);
            assertThat(isAccessibleCaptor.getValue()).isEqualTo(isAccessibleExpected);
            assertThat(isSafeCaptor.getValue()).isEqualTo(isSafeExpected);
            assertThat(isScenicCaptor.getValue()).isEqualTo(isScenicExpected);
            assertThat(isCleanCaptor.getValue()).isEqualTo(isCleanExpected);
            assertThat(isComfortableCaptor.getValue()).isEqualTo(isComfortableExpected);
        }
    }

    @Nested
    @DisplayName("createPlaceReviewImageList 메서드는")
    class CreatePlaceReviewImageList {
        Long placeId = 1L;
        Long placeReviewId = 1L;
        UserId userId = UserId.create();
        PlaceReview placeReview = PlaceReview.builder().build();
        List<MultipartFile> multipartFileList = List.of(new MockMultipartFile("file1", new byte[2]), new MockMultipartFile("file2", new byte[3]));
        List<File> fileList = multipartFileList.stream()
            .map(multipartFile -> File.builder().build())
            .collect(Collectors.toList());

        @Test
        @DisplayName("이미지를 추가할 장소 리뷰가 존재하지 않으면 예외가 발생한다.")
        void notFoundPlaceReviewException() {
            //given
            when(placeReviewQuery.findByPlaceIdAndId(placeId, placeReviewId, userId)).thenReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> placeService.createPlaceReviewImageList(userId, placeId, placeReviewId, multipartFileList))
                .isInstanceOf(NotFoundPlaceReviewException.class);
        }

        @Test
        @DisplayName("생성된 리뷰 이미지의 리뷰 필드를 검색한 리뷰로 설정한다.")
        void setReviewOfReviewImageToSearchReview() {
            //given
            when(placeReviewQuery.findByPlaceIdAndId(placeId, placeReviewId, userId)).thenReturn(Optional.of(placeReview));
            AtomicInteger ai = new AtomicInteger(0);
            multipartFileList.forEach(multipartFile -> {
                when(fileService.saveFileByMultipartFile(multipartFile, userId)).thenReturn(fileList.get(ai.getAndIncrement()));
            });

            //when
            placeService.createPlaceReviewImageList(userId, placeId, placeReviewId, multipartFileList);

            //then
            assertThat(placeReview.getPlaceReviewImageList().size())
                .isEqualTo(multipartFileList.size());

            placeReview.getPlaceReviewImageList()
                .forEach(reviewImage -> {
                    assertThat(reviewImage.getPlaceReview()).isEqualTo(placeReview);
                });
        }
    }

    @Nested
    @DisplayName("deletePlaceReviewImage 메서드는")
    class DeletePlaceReviewImage {
        Long placeId = 1L;
        Long placeReviewId = 1L;
        List<Long> placeReviewImageIdList = List.of(1L, 2L, 3L);
        UserId userId = UserId.create();

        @Test
        @DisplayName("요청을 보낸 유저가 해당 장소에 리뷰를 작성하지 않았으면 예외가 발생한다.")
        void notFoundPlaceReviewException() {
            //given
            when(placeReviewQuery.existsByPlaceIdAndReviewerUserId(placeId, userId)).thenReturn(false);

            //then
            assertThatThrownBy(() -> placeService.deletePlaceReviewImage(userId, placeId, placeReviewId, placeReviewImageIdList))
                .isInstanceOf(NotFoundPlaceReviewException.class);
        }
    }
}