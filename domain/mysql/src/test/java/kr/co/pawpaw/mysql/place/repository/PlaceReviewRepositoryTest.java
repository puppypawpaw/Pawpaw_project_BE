package kr.co.pawpaw.mysql.place.repository;

import kr.co.pawpaw.mysql.common.MySQLTestContainer;
import kr.co.pawpaw.mysql.place.domain.Place;
import kr.co.pawpaw.mysql.place.domain.PlaceReview;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PlaceReviewRepository 는")
class PlaceReviewRepositoryTest extends MySQLTestContainer {
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlaceReviewRepository placeReviewRepository;

    @Nested
    @DisplayName("deleteByPlaceIdAndReviewerUserId 메서드는")
    class deleteByPlaceIdAndReviewerUserId {
        Place place = Place.builder()
            .name("장소 이름")
            .build();

        Place place2 = Place.builder()
            .name("장소2 이름")
            .build();

        User user1 = User.builder()
            .email("user1@email.com")
            .name("user1-name")
            .build();

        User user2 = User.builder()
            .email("user2@email.com")
            .name("user2-name")
            .build();

        List<PlaceReview> placeReviewList;

        @BeforeEach
        void setup() {
            place = placeRepository.save(place);
            place2 = placeRepository.save(place2);
            user1 = userRepository.save(user1);
            user2 = userRepository.save(user2);
            placeReviewList = placeReviewRepository.saveAll(List.of(
                PlaceReview.builder()
                    .reviewer(user1)
                    .place(place)
                    .content("review1-content")
                    .build(),
                PlaceReview.builder()
                    .reviewer(user1)
                    .place(place2)
                    .content("review2-content")
                    .build(),
                PlaceReview.builder()
                    .reviewer(user2)
                    .place(place)
                    .content("review3-content")
                    .build(),
                PlaceReview.builder()
                    .reviewer(user2)
                    .place(place2)
                    .content("review4-content")
                    .build()
            ));
        }

        @Test
        @DisplayName("리뷰어의 유저 아이디와 장소 아이디로 리뷰를 삭제를 한다.")
        void deleteByReviewerUserIdAndPlaceId() {
            //given
            List<PlaceReview> resultExpected = placeReviewList.stream()
                .filter(review -> !(review.getPlace().equals(place) && review.getReviewer().equals(user1)))
                    .collect(Collectors.toList());

            //when
            placeReviewRepository.deleteByPlaceIdAndReviewerUserId(place.getId(), user1.getUserId());
            List<PlaceReview> result = placeReviewRepository.findAll();

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(resultExpected);
        }
    }
}