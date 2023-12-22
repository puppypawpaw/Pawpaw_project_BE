package kr.co.pawpaw.mysql.place.repository;

import kr.co.pawpaw.mysql.common.MySQLTestContainer;
import kr.co.pawpaw.mysql.common.domain.Position;
import kr.co.pawpaw.mysql.common.dto.PositionResponse;
import kr.co.pawpaw.mysql.config.QuerydslConfig;
import kr.co.pawpaw.mysql.place.domain.*;
import kr.co.pawpaw.mysql.place.dto.PlaceQueryDSLResponse;
import kr.co.pawpaw.mysql.place.dto.PlaceTopBookmarkPercentageResponse;
import kr.co.pawpaw.mysql.place.enums.PlaceType;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ChatroomCustomRepository 의")
@Import(value = {PlaceCustomRepository.class, QuerydslConfig.class})
class PlaceCustomRepositoryTest extends MySQLTestContainer {
    @Autowired
    private PlaceCustomRepository placeCustomRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private PlaceImageUrlRepository placeImageUrlRepository;
    @Autowired
    private PlaceReviewRepository placeReviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlaceBookmarkRepository placeBookmarkRepository;

    @Nested
    @DisplayName("findByQueryAndPlaceTypeAndPositionRange 메서드는")
    class FindByQueryAndPlaceTypeAndPositionRange {
        private List<Place> placeList = List.of(
            Place.builder()
                .placeType(PlaceType.RESTAURANT)
                .name("블루페이지")
                .position(Position.builder()
                    .latitude(37.5193292)
                    .longitude(127.0079764)
                    .address("서울특별시 서초구 잠원동 121-9 1층 블루페이지 라운지")
                    .build())
                .build(),
            Place.builder()
                .placeType(PlaceType.CAFE)
                .name("하다식당")
                .position(Position.builder()
                    .latitude(37.5324715)
                    .longitude(127.1239929)
                    .address("서울특별시 강동구 성내동 126-4 1층")
                    .build())
                .build(),
            Place.builder()
                .placeType(PlaceType.CAFE)
                .name("철판살롱")
                .position(Position.builder()
                    .latitude(37.5360048)
                    .longitude(127.1250743)
                    .address("서울특별시 강동구 성내동 38-9")
                    .build())
                .build(),
            Place.builder()
                .placeType(PlaceType.PARK)
                .name("우돈숯불명가")
                .position(Position.builder()
                    .latitude(37.546352)
                    .longitude(127.17076)
                    .address("서울특별시 강동구 상일동 309-1 1층 전체 전용주차5대")
                    .build())
                .build(),
            Place.builder()
                .placeType(PlaceType.PARK)
                .name("도깨비족발")
                .position(Position.builder()
                    .latitude(37.5381812)
                    .longitude(127.1312724)
                    .address("서울특별시 강동구 천호동 166-115 1층 도깨비족발")
                    .build())
                .build(),
            Place.builder()
                .placeType(PlaceType.PARK)
                .name("곱창팩토리 천호본점")
                .position(Position.builder()
                    .latitude(37.5397267)
                    .longitude(127.1281564)
                    .address("서울특별시 강동구 천호동 410-1 1층 곱창팩토리")
                    .build())
                .build()
        );

        List<PlaceBookmark> placeBookmarkList;

        List<PlaceImageUrl> placeImageUrlList;
        User user = User.builder().build();

        private final double latMin = -90.0;
        private final double latMax = 90.0;
        private final double longMin = -180.0;
        private final double longMax = 180.0;

        @BeforeEach
        void setup() {
            placeList = placeRepository.saveAll(placeList);
            placeImageUrlList = placeImageUrlRepository.saveAll(placeList.stream()
                .flatMap(place -> Stream.of(
                    PlaceImageUrl.builder()
                        .place(place)
                        .url(place.getName() + "-url1")
                        .build(),
                    PlaceImageUrl.builder()
                        .place(place)
                        .url(place.getName() + "-url2")
                        .build()
                )).collect(Collectors.toList()));
            user = userRepository.save(user);
            placeBookmarkList = placeBookmarkRepository.saveAll(List.of(PlaceBookmark.builder()
                .user(user)
                .place(placeList.get(0))
                .build()));
        }

        @AfterEach
        void afterSetup() {
            placeImageUrlRepository.deleteAll();
            placeBookmarkRepository.deleteAll();
            userRepository.deleteAll();
            placeRepository.deleteAll();
        }

        @ParameterizedTest
        @CsvSource(value = {"블루", "곱창", "팩토리", "null"}, nullValues = {"null"})
        @DisplayName("장소 이름 검색어로 장소를 검색할 수 있다.")
        @Transactional(propagation = Propagation.NOT_SUPPORTED)
        void searchByNameQuery(final String query) {
            //given
            List<PlaceQueryDSLResponse> resultExpected = placeList.stream()
                .filter(place -> Objects.isNull(query) || place.getName().contains(query))
                .map(place -> new PlaceQueryDSLResponse(
                    place.getId(),
                    placeImageUrlList.stream()
                        .filter(placeImageUrl -> placeImageUrl.getPlace().equals(place))
                        .map(PlaceImageUrl::getUrl)
                        .collect(Collectors.toSet()),
                    place.getName(),
                    PositionResponse.of(place.getPosition()),
                    Optional.ofNullable(place.getMonOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getMonOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getMonOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getTueOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getTueOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getTueOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getWedOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getWedOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getWedOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getThuOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getThuOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getThuOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getFriOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getFriOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getFriOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getSatOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getSatOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getSatOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getSunOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getSunOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getSunOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    placeBookmarkList.stream()
                        .map(PlaceBookmark::getPlace)
                        .collect(Collectors.toList()).contains(place),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                )).collect(Collectors.toList());

            //when
            List<PlaceQueryDSLResponse> result = placeCustomRepository.findByQueryAndPlaceTypeAndPositionRange(
                query,
                null,
                latMin,
                latMax,
                longMin,
                longMax,
                user.getUserId()
            );

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(resultExpected);
        }

        @ParameterizedTest
        @CsvSource(value = {"RESTAURANT", "CAFE", "PARK", "null"}, nullValues = {"null"})
        @DisplayName("장소 유형으로 장소를 검색할 수 있다.")
        void searchByPlaceType(final PlaceType placeType) {
            //given
            List<PlaceQueryDSLResponse> resultExpected = placeList.stream()
                .filter(place -> Objects.isNull(placeType) || place.getPlaceType().equals(placeType))
                .map(place -> new PlaceQueryDSLResponse(
                    place.getId(),
                    placeImageUrlList.stream()
                        .filter(placeImageUrl -> placeImageUrl.getPlace().equals(place))
                        .map(PlaceImageUrl::getUrl)
                        .collect(Collectors.toSet()),
                    place.getName(),
                    PositionResponse.of(place.getPosition()),
                    Optional.ofNullable(place.getMonOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getMonOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getMonOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getTueOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getTueOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getTueOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getWedOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getWedOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getWedOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getThuOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getThuOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getThuOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getFriOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getFriOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getFriOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getSatOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getSatOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getSatOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getSunOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getSunOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getSunOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    placeBookmarkList.stream()
                        .map(PlaceBookmark::getPlace)
                        .collect(Collectors.toList()).contains(place),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                )).collect(Collectors.toList());

            //when
            List<PlaceQueryDSLResponse> result = placeCustomRepository.findByQueryAndPlaceTypeAndPositionRange(
                null,
                placeType,
                latMin,
                latMax,
                longMin,
                longMax,
                user.getUserId()
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
            List<PlaceQueryDSLResponse> resultExpected = placeList.stream()
                .filter(place -> place.getPosition().isInside(latMin, latMax, longMin, longMax))
                .map(place -> new PlaceQueryDSLResponse(
                    place.getId(),
                    placeImageUrlList.stream()
                        .filter(placeImageUrl -> placeImageUrl.getPlace().equals(place))
                        .map(PlaceImageUrl::getUrl)
                        .collect(Collectors.toSet()),
                    place.getName(),
                    PositionResponse.of(place.getPosition()),
                    Optional.ofNullable(place.getMonOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getMonOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getMonOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getTueOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getTueOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getTueOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getWedOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getWedOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getWedOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getThuOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getThuOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getThuOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getFriOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getFriOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getFriOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getSatOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getSatOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getSatOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getSunOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getSunOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getSunOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    placeBookmarkList.stream()
                        .map(PlaceBookmark::getPlace)
                        .collect(Collectors.toList())
                        .contains(place),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                )).collect(Collectors.toList());

            //when
            List<PlaceQueryDSLResponse> result = placeCustomRepository.findByQueryAndPlaceTypeAndPositionRange(
                null,
                null,
                latMin,
                latMax,
                longMin,
                longMax,
                user.getUserId()
            );

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(resultExpected);
        }

        @Test
        @DisplayName("북마크 여부를 검사할 수 있다.")
        void checkBookmark() {
            //given
            List<PlaceQueryDSLResponse> resultExpected = placeList.stream()
                .map(place -> new PlaceQueryDSLResponse(
                    place.getId(),
                    placeImageUrlList.stream()
                        .filter(placeImageUrl -> placeImageUrl.getPlace().equals(place))
                        .map(PlaceImageUrl::getUrl)
                        .collect(Collectors.toSet()),
                    place.getName(),
                    PositionResponse.of(place.getPosition()),
                    Optional.ofNullable(place.getMonOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getMonOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getMonOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getTueOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getTueOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getTueOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getWedOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getWedOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getWedOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getThuOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getThuOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getThuOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getFriOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getFriOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getFriOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getSatOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getSatOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getSatOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    Optional.ofNullable(place.getSunOpenHour())
                        .map(DayOpenHour::getOpen)
                        .orElse(null),
                    Optional.ofNullable(place.getSunOpenHour())
                        .map(DayOpenHour::getClose)
                        .orElse(null),
                    Optional.ofNullable(place.getSunOpenHour())
                        .map(DayOpenHour::getLastOrder)
                        .orElse(null),
                    placeBookmarkList.stream()
                        .map(PlaceBookmark::getPlace)
                        .collect(Collectors.toList()).contains(place),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                )).collect(Collectors.toList());

            //when
            List<PlaceQueryDSLResponse> result = placeCustomRepository.findByQueryAndPlaceTypeAndPositionRange(
                null,
                null,
                latMin,
                latMax,
                longMin,
                longMax,
                user.getUserId()
            );

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(resultExpected);
        }
    }

    @Nested
    @DisplayName("updatePlaceReviewInfo 메서드는")
    class UpdatePlaceReviewInfo {
        Place place = Place.builder()
            .placeType(PlaceType.PARK)
            .name("우돈숯불명가")
            .position(Position.builder()
                .latitude(37.546352)
                .longitude(127.17076)
                .address("서울특별시 강동구 상일동 309-1 1층 전체 전용주차5대")
                .build())
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
                placeReview.isAccessible() ? 1 : 0,
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
                placeReview.isAccessible() ? 1 : 0,
                placeReview.isSafe() ? 1 : 0,
                placeReview.isScenic() ? 1 : 0,
                placeReview.isClean() ? 1 : 0,
                placeReview.isComfortable() ? 1 : 0
            );

            //then
            assertThat(place.getReviewInfo()).usingRecursiveComparison().isEqualTo(expectedResult);
        }
    }

    @Nested
    @DisplayName("findPlaceTopBookmarkPercentageList 메서드는")
    class FindPlaceTopBookmarkPercentageList {
        private List<Place> placeList = List.of(
            Place.builder()
                .placeType(PlaceType.RESTAURANT)
                .name("블루페이지")
                .position(Position.builder()
                    .latitude(37.5193292)
                    .longitude(127.0079764)
                    .address("서울특별시 서초구 잠원동 121-9 1층 블루페이지 라운지")
                    .build())
                .build(),
            Place.builder()
                .placeType(PlaceType.CAFE)
                .name("하다식당")
                .position(Position.builder()
                    .latitude(37.5324715)
                    .longitude(127.1239929)
                    .address("서울특별시 강동구 성내동 126-4 1층")
                    .build())
                .build(),
            Place.builder()
                .placeType(PlaceType.CAFE)
                .name("철판살롱")
                .position(Position.builder()
                    .latitude(37.5360048)
                    .longitude(127.1250743)
                    .address("서울특별시 강동구 성내동 38-9")
                    .build())
                .build(),
            Place.builder()
                .placeType(PlaceType.PARK)
                .name("우돈숯불명가")
                .position(Position.builder()
                    .latitude(37.546352)
                    .longitude(127.17076)
                    .address("서울특별시 강동구 상일동 309-1 1층 전체 전용주차5대")
                    .build())
                .build()
        );

        private List<User> userList = List.of(
            User.builder()
                .name("user-name-1")
                .position(Position.builder()
                    .address("서울특별시 강동구")
                    .latitude(36.8)
                    .longitude(36.7)
                    .build())
                .nickname("user-nickname-1")
                .phoneNumber("user-phoneNumber-1")
                .email("email1@liame.com")
                .build(),
            User.builder()
                .name("user-name-2")
                .position(Position.builder()
                    .address("서울특별시 강동구")
                    .latitude(36.8)
                    .longitude(36.7)
                    .build())
                .nickname("user-nickname-2")
                .phoneNumber("user-phoneNumber-2")
                .email("email2@liame.com")
                .build(),
            User.builder()
                .name("user-name-3")
                .position(Position.builder()
                    .address("서울특별시 강동구")
                    .latitude(36.8)
                    .longitude(36.7)
                    .build())
                .nickname("user-nickname-3")
                .phoneNumber("user-phoneNumber-3")
                .email("email3@liame.com")
                .build(),
            User.builder()
                .name("user-name-4")
                .position(Position.builder()
                    .address("서울특별시 강동구")
                    .latitude(36.8)
                    .longitude(36.7)
                    .build())
                .nickname("user-nickname-4")
                .phoneNumber("user-phoneNumber-4")
                .email("email4@liame.com")
                .build()
        );

        private List<PlaceBookmark> placeBookmarkList;

        @BeforeEach
        void setup() {
            userList = userRepository.saveAll(userList);
            placeList = placeRepository.saveAll(placeList);
            placeBookmarkList = placeBookmarkRepository.saveAll(List.of(
                PlaceBookmark.builder()
                    .user(userList.get(0))
                    .place(placeList.get(0))
                    .build(),
                PlaceBookmark.builder()
                    .user(userList.get(0))
                    .place(placeList.get(1))
                    .build(),
                PlaceBookmark.builder()
                    .user(userList.get(0))
                    .place(placeList.get(2))
                    .build(),
                PlaceBookmark.builder()
                    .user(userList.get(0))
                    .place(placeList.get(3))
                    .build(),
                PlaceBookmark.builder()
                    .user(userList.get(1))
                    .place(placeList.get(0))
                    .build(),
                PlaceBookmark.builder()
                    .user(userList.get(1))
                    .place(placeList.get(1))
                    .build(),
                PlaceBookmark.builder()
                    .user(userList.get(1))
                    .place(placeList.get(2))
                    .build(),
                PlaceBookmark.builder()
                    .user(userList.get(2))
                    .place(placeList.get(0))
                    .build(),
                PlaceBookmark.builder()
                    .user(userList.get(2))
                    .place(placeList.get(1))
                    .build(),
                PlaceBookmark.builder()
                    .user(userList.get(3))
                    .place(placeList.get(0))
                    .build()
            ));
        }

        @Test
        @DisplayName("입력받은 placeId 에 해당하는 place들의 순위 비율을 반환한다.")
        void returnPlaceTopBookmarkPercentage() {
            //given
            Collection<PlaceTopBookmarkPercentageResponse> expectedResult = List.of(
                new PlaceTopBookmarkPercentageResponse(placeList.get(0).getId(), 0.0),
                new PlaceTopBookmarkPercentageResponse(placeList.get(2).getId(), 0.6666666666666666)
            );

            //when
            Collection<PlaceTopBookmarkPercentageResponse> placeTopBookmarkPercentageResponses = placeCustomRepository.findPlaceTopBookmarkPercentageList(
                expectedResult.stream().map(PlaceTopBookmarkPercentageResponse::getPlaceId).collect(Collectors.toList())
            );

            //then
            assertThat(placeTopBookmarkPercentageResponses).usingRecursiveComparison().isEqualTo(expectedResult);
        }
    }
}