package kr.co.pawpaw.mysql.place.repository;

import kr.co.pawpaw.mysql.common.MySQLTestContainer;
import kr.co.pawpaw.mysql.common.domain.Position;
import kr.co.pawpaw.mysql.common.dto.PositionResponse;
import kr.co.pawpaw.mysql.config.QuerydslConfig;
import kr.co.pawpaw.mysql.place.domain.Place;
import kr.co.pawpaw.mysql.place.domain.PlaceReview;
import kr.co.pawpaw.mysql.place.domain.PlaceType;
import kr.co.pawpaw.mysql.place.domain.ReviewInfo;
import kr.co.pawpaw.mysql.place.dto.PlaceResponse;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ChatroomCustomRepository 의")
@Import(value = { PlaceCustomRepository.class, QuerydslConfig.class })
class PlaceCustomRepositoryTest extends MySQLTestContainer {
    @Autowired
    private PlaceCustomRepository placeCustomRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private PlaceReviewRepository placeReviewRepository;
    @Autowired
    private UserRepository userRepository;

    @Nested
    @DisplayName("findByQueryAndPlaceTypeAndPositionRange 메서드는")
    @Transactional(propagation = Propagation.NEVER)
    class FindByQueryAndPlaceTypeAndPositionRange {
        private List<Place> placeList = List.of(
            Place.builder()
                .placeImageUrls(List.of("place-1-imageUrl-1", "place-1-imageUrl-2"))
                .placeType(PlaceType.RESTAURANT)
                .name("블루페이지")
                .position(Position.builder()
                    .latitude(37.5193292)
                    .longitude(127.0079764)
                    .address("서울특별시 서초구 잠원동 121-9 1층 블루페이지 라운지")
                    .build())
                .openHours("항상 열려있습니다.")
                .build(),
            Place.builder()
                .placeImageUrls(List.of("place-2-imageUrl-1", "place-2-imageUrl-2"))
                .placeType(PlaceType.CAFE)
                .name("하다식당")
                .position(Position.builder()
                    .latitude(37.5324715)
                    .longitude(127.1239929)
                    .address("서울특별시 강동구 성내동 126-4 1층")
                    .build())
                .openHours("항상 닫혀있습니다.")
                .build(),
            Place.builder()
                .placeImageUrls(List.of("place-3-imageUrl-1", "place-3-imageUrl-2"))
                .placeType(PlaceType.CAFE)
                .name("철판살롱")
                .position(Position.builder()
                    .latitude(37.5360048)
                    .longitude(127.1250743)
                    .address("서울특별시 강동구 성내동 38-9")
                    .build())
                .openHours("항상 모릅니다.")
                .build(),
            Place.builder()
                .placeImageUrls(List.of("place-4-imageUrl-1", "place-4-imageUrl-2"))
                .placeType(PlaceType.PARK)
                .name("우돈숯불명가")
                .position(Position.builder()
                    .latitude(37.546352)
                    .longitude(127.17076)
                    .address("서울특별시 강동구 상일동 309-1 1층 전체 전용주차5대")
                    .build())
                .openHours("항상 압니다.")
                .build(),
            Place.builder()
                .placeImageUrls(List.of("place-5-imageUrl-1", "place-5-imageUrl-2"))
                .placeType(PlaceType.PARK)
                .name("도깨비족발")
                .position(Position.builder()
                    .latitude(37.5381812)
                    .longitude(127.1312724)
                    .address("서울특별시 강동구 천호동 166-115 1층 도깨비족발")
                    .build())
                .openHours("몰라요.")
                .build(),
            Place.builder()
                .placeImageUrls(List.of("place-6-imageUrl-1", "place-6-imageUrl-2"))
                .placeType(PlaceType.PARK)
                .name("곱창팩토리 천호본점")
                .position(Position.builder()
                    .latitude(37.5397267)
                    .longitude(127.1281564)
                    .address("서울특별시 강동구 천호동 410-1 1층 곱창팩토리")
                    .build())
                .openHours("알아요.")
                .build()
        );

        private final UserId userId = UserId.create();
        private final double latMin = -90.0;
        private final double latMax = 90.0;
        private final double longMin = -180.0;
        private final double longMax = 180.0;

        @BeforeEach
        void setup() {
            placeList = placeRepository.saveAll(placeList);
        }

        @AfterEach
        void afterSetup() {
            placeRepository.deleteAll();
        }

        @ParameterizedTest
        @CsvSource(value = {"블루", "곱창", "팩토리", "null"}, nullValues = {"null"})
        @DisplayName("장소 이름 검색어로 장소를 검색할 수 있다.")
        void searchByNameQuery(final String query) {
            //given
            List<PlaceResponse> resultExpected = placeList.stream()
                .filter(place -> Objects.isNull(query) || place.getName().contains(query))
                .map(place -> new PlaceResponse(
                    place.getId(),
                    place.getPlaceImageUrls(),
                    place.getName(),
                    PositionResponse.of(place.getPosition()),
                    place.getOpenHours(),
                    false,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                )).collect(Collectors.toList());

            //when
            List<PlaceResponse> result = placeCustomRepository.findByQueryAndPlaceTypeAndPositionRange(
                query,
                null,
                latMin,
                latMax,
                longMin,
                longMax,
                userId
            );

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(resultExpected);
        }

        @ParameterizedTest
        @CsvSource(value = {"RESTAURANT", "CAFE", "PARK", "null"}, nullValues = {"null"})
        @DisplayName("장소 유형으로 장소를 검색할 수 있다.")
        void searchByPlaceType(final PlaceType placeType) {
            //given
            List<PlaceResponse> resultExpected = placeList.stream()
                .filter(place -> Objects.isNull(placeType) || place.getPlaceType().equals(placeType))
                .map(place -> new PlaceResponse(
                    place.getId(),
                    place.getPlaceImageUrls(),
                    place.getName(),
                    PositionResponse.of(place.getPosition()),
                    place.getOpenHours(),
                    false,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                )).collect(Collectors.toList());

            //when
            List<PlaceResponse> result = placeCustomRepository.findByQueryAndPlaceTypeAndPositionRange(
                null,
                placeType,
                latMin,
                latMax,
                longMin,
                longMax,
                userId
            );

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(resultExpected);
        }

        @ParameterizedTest
        @CsvSource(value = {"37.519,37.539,127,128", "37.539,37.545,127,128", "37,38,127.12,127.129", "37,38,127.13,127.16", "-90,90,-180,180"})
        @DisplayName("장소 위경도로 장소를 검색할 수 있다.")
        void searchByLatitudeAndLongitude(
            final double latMin,
            final double latMax,
            final double longMin,
            final double longMax
        ) {
            //given
            List<PlaceResponse> resultExpected = placeList.stream()
                .filter(place -> place.getPosition().isInside(latMin, latMax, longMin, longMax))
                .map(place -> new PlaceResponse(
                    place.getId(),
                    place.getPlaceImageUrls(),
                    place.getName(),
                    PositionResponse.of(place.getPosition()),
                    place.getOpenHours(),
                    false,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                )).collect(Collectors.toList());

            //when
            List<PlaceResponse> result = placeCustomRepository.findByQueryAndPlaceTypeAndPositionRange(
                null,
                null,
                latMin,
                latMax,
                longMin,
                longMax,
                userId
            );

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(resultExpected);
        }
    }

    @Nested
    @DisplayName("updatePlaceReviewInfo 메서드는")
    class UpdatePlaceReviewInfo {
        Place place = Place.builder()
            .placeImageUrls(List.of("place-4-imageUrl-1", "place-4-imageUrl-2"))
            .placeType(PlaceType.PARK)
            .name("우돈숯불명가")
            .position(Position.builder()
                .latitude(37.546352)
                .longitude(127.17076)
                .address("서울특별시 강동구 상일동 309-1 1층 전체 전용주차5대")
                .build())
            .openHours("항상 압니다.")
            .build();

        User user = User.builder()
            .name("user-name-1")
            .position(Position.builder()
                .address("서울특별시 강동구")
                .latitude(36.8)
                .longitude(36.7)
                .build())
            .nickname("user-nickname-1")
            .phoneNumber("user-phoneNumber-1")
            .email("email1@liame.com")
            .build();

        PlaceReview placeReview;

        @BeforeEach
        void setup() {
            place = placeRepository.save(place);
            user = userRepository.save(user);
            placeReview = placeReviewRepository.save(PlaceReview.builder()
                    .place(place)
                    .reviewer(user)
                    .isQuiet(true)
                    .isClean(true)
                    .score(5L)
                    .content("굳")
                .build());
        }

        @Test
        @DisplayName("PlaceReview의 리뷰 내용을 place에 업데이트한다.")
        void updatePlaceByPlaceReview() {
            //given
            ReviewInfo expectedResult = ReviewInfo.builder()
                .totalScore(5)
                .reviewCnt(1)
                .quietCnt(1)
                .cleanCnt(1)
                .build();

            //when
            placeCustomRepository.updatePlaceReviewInfo(
                place.getId(),
                1,
                placeReview.getScore(),
                placeReview.isQuiet() ? 1 : 0,
                placeReview.isAccessible() ? 1: 0,
                placeReview.isSafe() ? 1 : 0,
                placeReview.isScenic() ? 1 : 0,
                placeReview.isClean() ? 1 : 0,
                placeReview.isComfortable() ? 1 : 0
            );

            Optional<Place> findPlace = placeRepository.findById(place.getId());

            //then
            assertThat(findPlace.isPresent()).isTrue();
            assertThat(findPlace.get().getReviewInfo()).usingRecursiveComparison().isEqualTo(expectedResult);
        }

        @Test
        @DisplayName("수행 후 영속성 컨텍스트에 변경내용이 반영된다.")
        void refreshPersistenceContext() {
            //given
            ReviewInfo expectedResult = ReviewInfo.builder()
                .totalScore(5)
                .reviewCnt(1)
                .quietCnt(1)
                .cleanCnt(1)
                .build();

            //when
            placeCustomRepository.updatePlaceReviewInfo(
                place.getId(),
                1,
                placeReview.getScore(),
                placeReview.isQuiet() ? 1 : 0,
                placeReview.isAccessible() ? 1: 0,
                placeReview.isSafe() ? 1 : 0,
                placeReview.isScenic() ? 1 : 0,
                placeReview.isClean() ? 1 : 0,
                placeReview.isComfortable() ? 1 : 0
            );

            //then
            assertThat(place.getReviewInfo()).usingRecursiveComparison().isEqualTo(expectedResult);
        }
    }
}