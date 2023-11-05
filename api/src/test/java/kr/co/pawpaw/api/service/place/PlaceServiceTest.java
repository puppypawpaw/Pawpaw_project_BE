package kr.co.pawpaw.api.service.place;

import kr.co.pawpaw.api.dto.place.CreatePlaceReviewRequest;
import kr.co.pawpaw.api.service.file.FileService;
import kr.co.pawpaw.common.exception.place.NotFoundPlaceException;
import kr.co.pawpaw.mysql.place.domain.Place;
import kr.co.pawpaw.mysql.place.domain.PlaceReview;
import kr.co.pawpaw.mysql.place.service.command.PlaceCommand;
import kr.co.pawpaw.mysql.place.service.command.PlaceReviewCommand;
import kr.co.pawpaw.mysql.place.service.query.PlaceQuery;
import kr.co.pawpaw.mysql.storage.domain.File;
import kr.co.pawpaw.mysql.user.domain.User;
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
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @InjectMocks
    private PlaceService placeService;

    @Nested
    @DisplayName("createPlaceReview 메서드는")
    class CreatePlaceReview {
        Long placeId = 1L;
        Place place = Place.builder().build();
        User user = User.builder().build();
        CreatePlaceReviewRequest request = CreatePlaceReviewRequest.builder()
            .isSafe(true)
            .score(5L)
            .content("안전해요")
            .build();

        File file = File.builder().build();

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

            //when
            assertThatThrownBy(() -> placeService.createPlaceReview(placeId, user.getUserId(), List.of(multipartFile), request))
                .isInstanceOf(NotFoundPlaceException.class);

            //then

        }

        @Test
        @DisplayName("PlaceReview 작성시 이미지를 업로드하지 않으면 PlaceReviewImage가 생성되지 않는다.(빈 리스트)")
        void ifNotUploadImageThenNotCreateReviewImageEmptyList() {
            //given
            when(userQuery.getReferenceById(user.getUserId())).thenReturn(user);
            when(placeQuery.findByPlaceId(place.getId())).thenReturn(Optional.of(place));

            //when
            placeService.createPlaceReview(placeId, user.getUserId(), List.of(), request);

            //then
            ArgumentCaptor<PlaceReview> placeReviewArgumentCaptor = ArgumentCaptor.forClass(PlaceReview.class);
            verify(placeReviewCommand).save(placeReviewArgumentCaptor.capture());
            assertThat(placeReviewArgumentCaptor.getValue().getPlaceReviewImageList().size()).isEqualTo(0);
        }

        @Test
        @DisplayName("PlaceReview 작성시 이미지를 업로드하지 않으면 PlaceReviewImage가 생성되지 않는다.(null 리스트)")
        void ifNotUploadImageThenNotCreateReviewImageNullList() {
            //given
            when(userQuery.getReferenceById(user.getUserId())).thenReturn(user);
            when(placeQuery.findByPlaceId(place.getId())).thenReturn(Optional.of(place));

            //when
            placeService.createPlaceReview(placeId, user.getUserId(), null, request);

            //then
            ArgumentCaptor<PlaceReview> placeReviewArgumentCaptor = ArgumentCaptor.forClass(PlaceReview.class);
            verify(placeReviewCommand).save(placeReviewArgumentCaptor.capture());
            assertThat(placeReviewArgumentCaptor.getValue().getPlaceReviewImageList().size()).isEqualTo(0);
        }

        @Test
        @DisplayName("PlaceReview객체 안에 있는 PlaceReviewImageList는 PlaceReview를 참조한다.")
        void placeReviewImageRefPlaceReview() {
            //given
            when(userQuery.getReferenceById(user.getUserId())).thenReturn(user);
            when(placeQuery.findByPlaceId(place.getId())).thenReturn(Optional.of(place));
            when(fileService.saveFileByMultipartFile(multipartFile, user.getUserId())).thenReturn(file);

            //when
            placeService.createPlaceReview(placeId, user.getUserId(), List.of(multipartFile), request);

            //then
            ArgumentCaptor<PlaceReview> placeReviewArgumentCaptor = ArgumentCaptor.forClass(PlaceReview.class);
            verify(placeReviewCommand).save(placeReviewArgumentCaptor.capture());
            PlaceReview placeReview = placeReviewArgumentCaptor.getValue();
            assertThat(placeReview.getPlaceReviewImageList().size()).isEqualTo(1);
            assertThat(placeReview.getPlaceReviewImageList().stream().filter(placeReviewImage -> placeReviewImage.getPlaceReview().equals(placeReview)).count()).isEqualTo(1);
        }
    }
}