package kr.co.pawpaw.mysql.place.repository;

import kr.co.pawpaw.mysql.common.MySQLTestContainer;
import kr.co.pawpaw.mysql.common.domain.Position;
import kr.co.pawpaw.mysql.config.QuerydslConfig;
import kr.co.pawpaw.mysql.place.domain.Place;
import kr.co.pawpaw.mysql.place.domain.PlaceReview;
import kr.co.pawpaw.mysql.place.domain.PlaceReviewImage;
import kr.co.pawpaw.mysql.place.dto.PlaceReviewResponse;
import kr.co.pawpaw.mysql.storage.domain.File;
import kr.co.pawpaw.mysql.storage.repository.FileRepository;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Slice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Import(value = {QuerydslConfig.class, PlaceReviewCustomRepository.class})
@DisplayName("PlaceReviewCustomRepository 는")
class PlaceReviewCustomRepositoryTest extends MySQLTestContainer {
    @Autowired
    private PlaceReviewCustomRepository placeReviewCustomRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PlaceReviewRepository placeReviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    @Nested
    @DisplayName("findByPlaceIdAndIdBefore 메서드는")
    class FindByPlaceIdAndIdBefore {
        User me = User.builder()
            .position(Position.builder()
                .address("서울특별시 강동구")
                .latitude(36.8)
                .longitude(36.7)
                .build())
            .build();
        User user1 = User.builder()
            .position(Position.builder()
                .address("서울특별시 강동구")
                .latitude(36.8)
                .longitude(36.7)
                .build())
            .build();
        User user2 = User.builder()
            .position(Position.builder()
                .address("서울특별시 강동구")
                .latitude(36.8)
                .longitude(36.7)
                .build())
            .build();
        User user3 = User.builder()
            .position(Position.builder()
                .address("서울특별시 강동구")
                .latitude(36.8)
                .longitude(36.7)
                .build())
            .build();
        User user4 = User.builder()
            .position(Position.builder()
                .address("서울특별시 강동구")
                .latitude(36.8)
                .longitude(36.7)
                .build())
            .build();

        Place place = Place.builder()
            .name("장소이름")
            .build();

        PlaceReview myReview = PlaceReview.builder()
            .place(place)
            .reviewer(me)
            .isAccessible(true)
            .isComfortable(true)
            .score(5L)
            .content("my-content")
            .build();

        List<PlaceReviewImage> myReviewImage = new ArrayList<>();

        PlaceReview user1Review = PlaceReview.builder()
            .place(place)
            .reviewer(user1)
            .isAccessible(true)
            .isComfortable(true)
            .score(4L)
            .content("user1-content")
            .build();

        List<PlaceReviewImage> user1ReviewImage = new ArrayList<>();

        PlaceReview user2Review = PlaceReview.builder()
            .place(place)
            .reviewer(user2)
            .isAccessible(true)
            .isComfortable(true)
            .score(3L)
            .content("user2-content")
            .build();

        List<PlaceReviewImage> user2ReviewImage = new ArrayList<>();

        PlaceReview user3Review = PlaceReview.builder()
            .place(place)
            .reviewer(user3)
            .isAccessible(true)
            .isComfortable(true)
            .score(2L)
            .content("user3-content")
            .build();

        List<PlaceReviewImage> user3ReviewImage = new ArrayList<>();

        PlaceReview user4Review = PlaceReview.builder()
            .place(place)
            .reviewer(user4)
            .isAccessible(true)
            .isComfortable(true)
            .score(1L)
            .content("user4-content")
            .build();

        List<PlaceReviewImage> user4ReviewImage = new ArrayList<>();

        @BeforeEach
        void setup() {
            me.updateImage(fileRepository.save(File.builder()
                .fileName("me-image-fileName")
                .byteSize(1L)
                .contentType("image/jpeg")
                .fileUrl("me-image-url")
                .build()));
            user1.updateImage(fileRepository.save(File.builder()
                .fileName("user1-image-fileName")
                .byteSize(1L)
                .contentType("image/jpeg")
                .fileUrl("user1-image-url")
                .build()));
            user2.updateImage(fileRepository.save(File.builder()
                .fileName("user2-image-fileName")
                .byteSize(1L)
                .contentType("image/jpeg")
                .fileUrl("user2-image-url")
                .build()));
            user3.updateImage(fileRepository.save(File.builder()
                .fileName("user3-image-fileName")
                .byteSize(1L)
                .contentType("image/jpeg")
                .fileUrl("user3-image-url")
                .build()));
            user4.updateImage(fileRepository.save(File.builder()
                .fileName("user4-image-fileName")
                .byteSize(1L)
                .contentType("image/jpeg")
                .fileUrl("user4-image-url")
                .build()));

            me = userRepository.save(me);
            user1 = userRepository.save(user1);
            user2 = userRepository.save(user2);
            user3 = userRepository.save(user3);
            user4 = userRepository.save(user4);

            place = placeRepository.save(place);

            File user2ReviewImageFile1 = fileRepository.save(File.builder()
                .fileName("name")
                .byteSize(1L)
                .contentType("image/jpeg")
                .fileUrl("user2ReviewImageFile1Url")
                .build());

            File user2ReviewImageFile2 = fileRepository.save(File.builder()
                .fileName("name")
                .byteSize(1L)
                .contentType("image/jpeg")
                .fileUrl("user2ReviewImageFile2Url")
                .build());

            user2ReviewImage.addAll(List.of(
                PlaceReviewImage.builder()
                    .image(user2ReviewImageFile1)
                    .build(),
                PlaceReviewImage.builder()
                    .image(user2ReviewImageFile2)
                    .build()));

            user2Review.addReviewImageList(user2ReviewImage);

            myReview = placeReviewRepository.save(myReview);
            user1Review = placeReviewRepository.save(user1Review);
            user2Review = placeReviewRepository.save(user2Review);
            user3Review = placeReviewRepository.save(user3Review);
            user4Review = placeReviewRepository.save(user4Review);
        }

        @Test
        @DisplayName("size가 전체 리뷰 숫자보다 크거나 같으면 hasNext가 false다")
        void ifSizeIsGeThanTotalReviewCntThenHasNextIsFalse() {
            //when
            Slice<PlaceReviewResponse> sameSizeResult = placeReviewCustomRepository.findByPlaceIdAndIdBefore(me.getUserId(), place.getId(), null, 4);
            Slice<PlaceReviewResponse> bigSizeResult = placeReviewCustomRepository.findByPlaceIdAndIdBefore(me.getUserId(), place.getId(), null, 5);

            //then
            assertThat(sameSizeResult.hasNext()).isEqualTo(false);
            assertThat(bigSizeResult.hasNext()).isEqualTo(false);
        }

        @Test
        @DisplayName("size가 전체 리뷰 숫자보다 작으면 hasNext가 true다.")
        void ifSizeIsLtThanTotalReviewCntThenHasNextIsTrue() {
            //when
            Slice<PlaceReviewResponse> smallSizeResult = placeReviewCustomRepository.findByPlaceIdAndIdBefore(me.getUserId(), place.getId(), null, 3);

            //then
            assertThat(smallSizeResult.hasNext()).isEqualTo(true);
        }


        @Test
        @DisplayName("이전 리뷰 아이디가 있으면 이전 리뷰 아이디 보다 작은 ID를 가진 리뷰가 보인다.")
        void ifBeforeReviewIdNonNullThenShowReviewHasLittleIdThanBeforeReviewId() {
            //when
            Slice<PlaceReviewResponse> result = placeReviewCustomRepository.findByPlaceIdAndIdBefore(me.getUserId(), place.getId(), user3Review.getId(), 10);

            //then
            assertThat(result.getContent()
                .size()
            ).isEqualTo(2);
            assertThat(result.getContent()
                .stream()
                .filter(review -> review.getPlaceReviewId() < user3Review.getId())
                .count()
            ).isEqualTo(2);
        }

        @Test
        @DisplayName("본인 리뷰는 제외하고 조회된다.")
        void excludeMyReview() {
            //given
            List<PlaceReviewResponse> result1Expected = List.of(
                new PlaceReviewResponse(
                    user3Review.getId(),
                    user3.getUserId(),
                    user3.getNickname(),
                    user3.getBriefIntroduction(),
                    user3.getUserImage().getFileUrl(),
                    user3ReviewImage.stream()
                        .map(image -> image.getImage().getFileUrl())
                        .collect(Collectors.toList()),
                    user3Review.getScore(),
                    user3Review.isScenic(),
                    user3Review.isQuiet(),
                    user3Review.isComfortable(),
                    user3Review.isAccessible(),
                    user3Review.isClean(),
                    user3Review.isSafe(),
                    user3Review.getContent()
                ),
                new PlaceReviewResponse(
                    user2Review.getId(),
                    user2.getUserId(),
                    user2.getNickname(),
                    user2.getBriefIntroduction(),
                    user2.getUserImage().getFileUrl(),
                    user2ReviewImage.stream()
                        .map(image -> image.getImage().getFileUrl())
                        .collect(Collectors.toList()),
                    user2Review.getScore(),
                    user2Review.isScenic(),
                    user2Review.isQuiet(),
                    user2Review.isComfortable(),
                    user2Review.isAccessible(),
                    user2Review.isClean(),
                    user2Review.isSafe(),
                    user2Review.getContent()
                ),
                new PlaceReviewResponse(
                    user1Review.getId(),
                    user1.getUserId(),
                    user1.getNickname(),
                    user1.getBriefIntroduction(),
                    user1.getUserImage().getFileUrl(),
                    user1ReviewImage.stream()
                        .map(image -> image.getImage().getFileUrl())
                        .collect(Collectors.toList()),
                    user1Review.getScore(),
                    user1Review.isScenic(),
                    user1Review.isQuiet(),
                    user1Review.isComfortable(),
                    user1Review.isAccessible(),
                    user1Review.isClean(),
                    user1Review.isSafe(),
                    user1Review.getContent()
                )
            );

            List<PlaceReviewResponse> result2Expected = List.of(
                new PlaceReviewResponse(
                    user4Review.getId(),
                    user4.getUserId(),
                    user4.getNickname(),
                    user4.getBriefIntroduction(),
                    user4.getUserImage().getFileUrl(),
                    user4ReviewImage.stream()
                        .map(image -> image.getImage().getFileUrl())
                        .collect(Collectors.toList()),
                    user4Review.getScore(),
                    user4Review.isScenic(),
                    user4Review.isQuiet(),
                    user4Review.isComfortable(),
                    user4Review.isAccessible(),
                    user4Review.isClean(),
                    user4Review.isSafe(),
                    user4Review.getContent()
                ),
                new PlaceReviewResponse(
                    user3Review.getId(),
                    user3.getUserId(),
                    user3.getNickname(),
                    user3.getBriefIntroduction(),
                    user3.getUserImage().getFileUrl(),
                    user3ReviewImage.stream()
                        .map(image -> image.getImage().getFileUrl())
                        .collect(Collectors.toList()),
                    user3Review.getScore(),
                    user3Review.isScenic(),
                    user3Review.isQuiet(),
                    user3Review.isComfortable(),
                    user3Review.isAccessible(),
                    user3Review.isClean(),
                    user3Review.isSafe(),
                    user3Review.getContent()
                ),
                new PlaceReviewResponse(
                    user2Review.getId(),
                    user2.getUserId(),
                    user2.getNickname(),
                    user2.getBriefIntroduction(),
                    user2.getUserImage().getFileUrl(),
                    user2ReviewImage.stream()
                        .map(image -> image.getImage().getFileUrl())
                        .collect(Collectors.toList()),
                    user2Review.getScore(),
                    user2Review.isScenic(),
                    user2Review.isQuiet(),
                    user2Review.isComfortable(),
                    user2Review.isAccessible(),
                    user2Review.isClean(),
                    user2Review.isSafe(),
                    user2Review.getContent()
                ),
                new PlaceReviewResponse(
                    user1Review.getId(),
                    user1.getUserId(),
                    user1.getNickname(),
                    user1.getBriefIntroduction(),
                    user1.getUserImage().getFileUrl(),
                    user1ReviewImage.stream()
                        .map(image -> image.getImage().getFileUrl())
                        .collect(Collectors.toList()),
                    user1Review.getScore(),
                    user1Review.isScenic(),
                    user1Review.isQuiet(),
                    user1Review.isComfortable(),
                    user1Review.isAccessible(),
                    user1Review.isClean(),
                    user1Review.isSafe(),
                    user1Review.getContent()
                )
            );

            //when
            List<PlaceReviewResponse> result1 = placeReviewCustomRepository.findByPlaceIdAndIdBefore(me.getUserId(), place.getId(), user4Review.getId(), 10).getContent();
            List<PlaceReviewResponse> result2 = placeReviewCustomRepository.findByPlaceIdAndIdBefore(me.getUserId(), place.getId(), null, 10).getContent();

            //then
            assertThat(result1).usingRecursiveComparison().isEqualTo(result1Expected);
            assertThat(result2).usingRecursiveComparison().isEqualTo(result2Expected);
        }
    }

    @Nested
    @DisplayName("findByPlaceIdAndReviewerUserId 메서드는")
    class FindByPlaceIdAndReviewerUserId {
        User me = User.builder()
            .position(Position.builder()
                .address("서울특별시 강동구")
                .latitude(36.8)
                .longitude(36.7)
                .build())
            .build();
        User user1 = User.builder()
            .position(Position.builder()
                .address("서울특별시 강동구")
                .latitude(36.8)
                .longitude(36.7)
                .build())
            .build();
        User user2 = User.builder()
            .position(Position.builder()
                .address("서울특별시 강동구")
                .latitude(36.8)
                .longitude(36.7)
                .build())
            .build();
        User user3 = User.builder()
            .position(Position.builder()
                .address("서울특별시 강동구")
                .latitude(36.8)
                .longitude(36.7)
                .build())
            .build();
        User user4 = User.builder()
            .position(Position.builder()
                .address("서울특별시 강동구")
                .latitude(36.8)
                .longitude(36.7)
                .build())
            .build();

        Place place = Place.builder()
            .name("장소이름")
            .build();

        PlaceReview myReview = PlaceReview.builder()
            .place(place)
            .reviewer(me)
            .isAccessible(true)
            .isComfortable(true)
            .score(5L)
            .content("my-content")
            .build();

        List<PlaceReviewImage> myReviewImage = new ArrayList<>();

        PlaceReview user1Review = PlaceReview.builder()
            .place(place)
            .reviewer(user1)
            .isAccessible(true)
            .isComfortable(true)
            .score(4L)
            .content("user1-content")
            .build();

        List<PlaceReviewImage> user1ReviewImage = new ArrayList<>();

        PlaceReview user2Review = PlaceReview.builder()
            .place(place)
            .reviewer(user2)
            .isAccessible(true)
            .isComfortable(true)
            .score(3L)
            .content("user2-content")
            .build();

        List<PlaceReviewImage> user2ReviewImage = new ArrayList<>();

        PlaceReview user3Review = PlaceReview.builder()
            .place(place)
            .reviewer(user3)
            .isAccessible(true)
            .isComfortable(true)
            .score(2L)
            .content("user3-content")
            .build();

        List<PlaceReviewImage> user3ReviewImage = new ArrayList<>();

        PlaceReview user4Review = PlaceReview.builder()
            .place(place)
            .reviewer(user4)
            .isAccessible(true)
            .isComfortable(true)
            .score(1L)
            .content("user4-content")
            .build();

        List<PlaceReviewImage> user4ReviewImage = new ArrayList<>();

        @BeforeEach
        void setup() {
            me.updateImage(fileRepository.save(File.builder()
                .fileName("me-image-fileName")
                .byteSize(1L)
                .contentType("image/jpeg")
                .fileUrl("me-image-url")
                .build()));
            user1.updateImage(fileRepository.save(File.builder()
                .fileName("user1-image-fileName")
                .byteSize(1L)
                .contentType("image/jpeg")
                .fileUrl("user1-image-url")
                .build()));
            user2.updateImage(fileRepository.save(File.builder()
                .fileName("user2-image-fileName")
                .byteSize(1L)
                .contentType("image/jpeg")
                .fileUrl("user2-image-url")
                .build()));
            user3.updateImage(fileRepository.save(File.builder()
                .fileName("user3-image-fileName")
                .byteSize(1L)
                .contentType("image/jpeg")
                .fileUrl("user3-image-url")
                .build()));
            user4.updateImage(fileRepository.save(File.builder()
                .fileName("user4-image-fileName")
                .byteSize(1L)
                .contentType("image/jpeg")
                .fileUrl("user4-image-url")
                .build()));

            me = userRepository.save(me);
            user1 = userRepository.save(user1);
            user2 = userRepository.save(user2);
            user3 = userRepository.save(user3);
            user4 = userRepository.save(user4);

            place = placeRepository.save(place);

            File user2ReviewImageFile1 = fileRepository.save(File.builder()
                .fileName("name")
                .byteSize(1L)
                .contentType("image/jpeg")
                .fileUrl("user2ReviewImageFile1Url")
                .build());

            File user2ReviewImageFile2 = fileRepository.save(File.builder()
                .fileName("name")
                .byteSize(1L)
                .contentType("image/jpeg")
                .fileUrl("user2ReviewImageFile2Url")
                .build());

            user2ReviewImage.addAll(List.of(
                PlaceReviewImage.builder()
                    .image(user2ReviewImageFile1)
                    .build(),
                PlaceReviewImage.builder()
                    .image(user2ReviewImageFile2)
                    .build()));

            user2Review.addReviewImageList(user2ReviewImage);

            myReview = placeReviewRepository.save(myReview);
            user1Review = placeReviewRepository.save(user1Review);
            user2Review = placeReviewRepository.save(user2Review);
            user3Review = placeReviewRepository.save(user3Review);
            user4Review = placeReviewRepository.save(user4Review);
        }

        @Test
        @DisplayName("본인 리뷰만 조회한다.")
        void findOnlyMyReview() {
            //given
            PlaceReviewResponse result1Expected = new PlaceReviewResponse(
                myReview.getId(),
                me.getUserId(),
                me.getNickname(),
                me.getBriefIntroduction(),
                me.getUserImage().getFileUrl(),
                myReviewImage.stream()
                    .map(image -> image.getImage().getFileUrl())
                    .collect(Collectors.toList()),
                myReview.getScore(),
                myReview.isScenic(),
                myReview.isQuiet(),
                myReview.isComfortable(),
                myReview.isAccessible(),
                myReview.isClean(),
                myReview.isSafe(),
                myReview.getContent()
            );

            PlaceReviewResponse result2Expected = new PlaceReviewResponse(
                user1Review.getId(),
                user1.getUserId(),
                user1.getNickname(),
                user1.getBriefIntroduction(),
                user1.getUserImage().getFileUrl(),
                user1ReviewImage.stream()
                    .map(image -> image.getImage().getFileUrl())
                    .collect(Collectors.toList()),
                user1Review.getScore(),
                user1Review.isScenic(),
                user1Review.isQuiet(),
                user1Review.isComfortable(),
                user1Review.isAccessible(),
                user1Review.isClean(),
                user1Review.isSafe(),
                user1Review.getContent()
            );

            PlaceReviewResponse result3Expected = new PlaceReviewResponse(
                user2Review.getId(),
                user2.getUserId(),
                user2.getNickname(),
                user2.getBriefIntroduction(),
                user2.getUserImage().getFileUrl(),
                user2ReviewImage.stream()
                    .map(image -> image.getImage().getFileUrl())
                    .collect(Collectors.toList()),
                user2Review.getScore(),
                user2Review.isScenic(),
                user2Review.isQuiet(),
                user2Review.isComfortable(),
                user2Review.isAccessible(),
                user2Review.isClean(),
                user2Review.isSafe(),
                user2Review.getContent()
            );

            PlaceReviewResponse result4Expected = new PlaceReviewResponse(
                user3Review.getId(),
                user3.getUserId(),
                user3.getNickname(),
                user3.getBriefIntroduction(),
                user3.getUserImage().getFileUrl(),
                user3ReviewImage.stream()
                    .map(image -> image.getImage().getFileUrl())
                    .collect(Collectors.toList()),
                user3Review.getScore(),
                user3Review.isScenic(),
                user3Review.isQuiet(),
                user3Review.isComfortable(),
                user3Review.isAccessible(),
                user3Review.isClean(),
                user3Review.isSafe(),
                user3Review.getContent()
            );

            PlaceReviewResponse result5Expected = new PlaceReviewResponse(
                user4Review.getId(),
                user4.getUserId(),
                user4.getNickname(),
                user4.getBriefIntroduction(),
                user4.getUserImage().getFileUrl(),
                user4ReviewImage.stream()
                    .map(image -> image.getImage().getFileUrl())
                    .collect(Collectors.toList()),
                user4Review.getScore(),
                user4Review.isScenic(),
                user4Review.isQuiet(),
                user4Review.isComfortable(),
                user4Review.isAccessible(),
                user4Review.isClean(),
                user4Review.isSafe(),
                user4Review.getContent()
            );

            //when
            PlaceReviewResponse result1 = placeReviewCustomRepository.findByPlaceIdAndReviewerUserId(place.getId(), me.getUserId());
            PlaceReviewResponse result2 = placeReviewCustomRepository.findByPlaceIdAndReviewerUserId(place.getId(), user1.getUserId());
            PlaceReviewResponse result3 = placeReviewCustomRepository.findByPlaceIdAndReviewerUserId(place.getId(), user2.getUserId());
            PlaceReviewResponse result4 = placeReviewCustomRepository.findByPlaceIdAndReviewerUserId(place.getId(), user3.getUserId());
            PlaceReviewResponse result5 = placeReviewCustomRepository.findByPlaceIdAndReviewerUserId(place.getId(), user4.getUserId());

            //then
            assertThat(result1).usingRecursiveComparison().isEqualTo(result1Expected);
            assertThat(result2).usingRecursiveComparison().isEqualTo(result2Expected);
            assertThat(result3).usingRecursiveComparison().isEqualTo(result3Expected);
            assertThat(result4).usingRecursiveComparison().isEqualTo(result4Expected);
            assertThat(result5).usingRecursiveComparison().isEqualTo(result5Expected);
        }

        @Test
        @DisplayName("리뷰를 작성하지 않은 유저가 조회를 하면 null을 반환한다.")
        void ifUserNotPostReviewAndFindThenReturnNull() {
            //then
            assertThat(placeReviewCustomRepository.findByPlaceIdAndReviewerUserId(place.getId(), UserId.create()))
                .isNull();

        }
    }
}