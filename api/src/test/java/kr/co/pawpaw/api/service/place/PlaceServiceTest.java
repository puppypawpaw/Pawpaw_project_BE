package kr.co.pawpaw.api.service.place;

import kr.co.pawpaw.api.dto.place.CreatePlaceReviewRequest;
import kr.co.pawpaw.api.service.file.FileService;
import kr.co.pawpaw.common.exception.place.AlreadyPlaceReviewExistsException;
import kr.co.pawpaw.common.exception.place.NotFoundPlaceException;
import kr.co.pawpaw.common.exception.place.NotFoundPlaceReviewException;
import kr.co.pawpaw.mysql.place.domain.Place;
import kr.co.pawpaw.mysql.place.domain.PlaceReview;
import kr.co.pawpaw.mysql.place.service.command.PlaceCommand;
import kr.co.pawpaw.mysql.place.service.command.PlaceReviewCommand;
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
    private PlaceReviewCommand placeReviewCommand;
    @Mock
    private FileService fileService;
    @Mock
    private MultipartFile multipartFile;
    @Mock
    private PlaceReviewQuery placeReviewQuery;

    @InjectMocks
    private PlaceService placeService;

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
            assertThatThrownBy(() -> placeService.createPlaceReview(placeId, user.getUserId(), request))
                .isInstanceOf(NotFoundPlaceException.class);

        }

        @Test
        @DisplayName("이미 리뷰를 작성한 장소이면 예외가 발생한다.")
        void AlreadyPlaceReviewExistsException() {
            //given
            when(userQuery.getReferenceById(user.getUserId())).thenReturn(user);
            when(placeQuery.findByPlaceId(place.getId())).thenReturn(Optional.of(place));
            when(placeReviewQuery.existsByPlaceIdAndReviewerUserId(place.getId(), user.getUserId())).thenReturn(true);

            //then
            assertThatThrownBy(() -> placeService.createPlaceReview(placeId, user.getUserId(), request))
                .isInstanceOf(AlreadyPlaceReviewExistsException.class);
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
}