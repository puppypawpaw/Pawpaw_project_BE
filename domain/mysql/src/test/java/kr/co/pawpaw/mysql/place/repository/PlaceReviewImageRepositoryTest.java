package kr.co.pawpaw.mysql.place.repository;

import kr.co.pawpaw.mysql.common.MySQLTestContainer;
import kr.co.pawpaw.mysql.common.domain.Position;
import kr.co.pawpaw.mysql.place.domain.Place;
import kr.co.pawpaw.mysql.place.domain.PlaceReview;
import kr.co.pawpaw.mysql.place.domain.PlaceReviewImage;
import kr.co.pawpaw.mysql.place.enums.PlaceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PlaceReviewImageRepository 의")
class PlaceReviewImageRepositoryTest extends MySQLTestContainer {
    @Autowired
    PlaceReviewImageRepository placeReviewImageRepository;

    @Autowired
    PlaceReviewRepository placeReviewRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Nested
    @DisplayName("deleteByPlaceReviewIdAndIdIn 메서드는")
    class DeleteByPlaceReviewIdAndIdIn {
        Place placeToReview = Place.builder()
            .placeType(PlaceType.RESTAURANT)
            .name("placeName")
            .position(Position.builder()
                .address("안알랴줌")
                .latitude(37.4)
                .longitude(127.3)
                .build())
            .build();

        List<PlaceReview> placeReviewList = new ArrayList<>();
        List<PlaceReviewImage> placeReviewImageList = new ArrayList<>();

        @BeforeEach
        void setup() {
            placeToReview = placeRepository.save(placeToReview);
            placeReviewList.addAll(placeReviewRepository.saveAll(List.of(
                PlaceReview.builder()
                    .place(placeToReview)
                    .content("review1")
                    .build(),
                PlaceReview.builder()
                    .place(placeToReview)
                    .content("review2")
                    .build()
            )));

            placeReviewImageList.addAll(placeReviewImageRepository.saveAll(List.of(
                PlaceReviewImage.builder()
                    .placeReview(placeReviewList.get(0))
                    .build(),
                PlaceReviewImage.builder()
                    .placeReview(placeReviewList.get(0))
                    .build(),
                PlaceReviewImage.builder()
                    .placeReview(placeReviewList.get(1))
                    .build(),
                PlaceReviewImage.builder()
                    .placeReview(placeReviewList.get(1))
                    .build()
            )));
        }

        @ParameterizedTest
        @CsvSource(value = { "0", "1" })
        @DisplayName("리뷰 아이디와 리뷰 이미지 아이디 목록에 해당하는 모든 엔티티를 삭제한다.")
        void removeAllEntityThatReviewIdAndReviewImageIdIsSame(int deleteIndex) {
            //given
            long expectedRemainCount = placeReviewImageList.stream()
                .filter(image -> !image.getPlaceReview().equals(placeReviewList.get(deleteIndex)))
                .count();

            //when
            placeReviewImageRepository.deleteByPlaceReviewIdAndIdIn(
                placeReviewList.get(deleteIndex).getId(),
                placeReviewImageList.stream()
                    .map(PlaceReviewImage::getId)
                    .collect(Collectors.toList())
            );

            List<PlaceReviewImage> placeReviewImageList = placeReviewImageRepository.findAll();

            //then
            assertThat(placeReviewImageList.stream()
                .filter(image -> !image.getPlaceReview().equals(placeReviewList.get(deleteIndex)))
                .count())
                .isEqualTo(expectedRemainCount);
        }
    }
}