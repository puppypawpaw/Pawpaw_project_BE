package kr.co.pawpaw.mysql.place.service.query;

import kr.co.pawpaw.mysql.place.domain.PlaceType;
import kr.co.pawpaw.mysql.place.repository.PlaceCustomRepository;
import kr.co.pawpaw.mysql.user.domain.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@DisplayName("PlaceQuery의")
@ExtendWith(MockitoExtension.class)
class PlaceQueryTest {

    @Nested
    @DisplayName("findByQueryAndPlaceTypeAndPositionRange 메서드는")
    class FindByQueryAndPlaceTypeAndPositionRange {
        @Mock
        private PlaceCustomRepository placeCustomRepository;
        @InjectMocks
        private PlaceQuery placeQuery;

        @ParameterizedTest
        @CsvSource(value = {
            "null,null,null,null,-90,90,-180,180",
            "null,40,null,30,-90,40,-180,30",
            "35,null,32,null,35,90,32,180",
            "30,40,-50,50,30,40,-50,50"
        }, nullValues={"null"})
        @DisplayName("위도또는 경도의 상하한을 입력하지 않으면 상한과 하한으로 변경한다.")
        void ifLatitudeIsNullThenChangeLatitude(
            final Double latitudeMin,
            final Double latitudeMax,
            final Double longitudeMin,
            final Double longitudeMax,
            final Double latitudeMinExpect,
            final Double latitudeMaxExpect,
            final Double longitudeMinExpect,
            final Double longitudeMaxExpect
        ) {
            //given
            String query = "query";
            PlaceType placeType = PlaceType.PARK;
            UserId userId = UserId.create();

            //when
            placeQuery.findByQueryAndPlaceTypeAndPositionRange(
                query,
                placeType,
                latitudeMin,
                latitudeMax,
                longitudeMin,
                longitudeMax,
                userId
            );

            //then
            verify(placeCustomRepository).findByQueryAndPlaceTypeAndPositionRange(
                query,
                placeType,
                latitudeMinExpect,
                latitudeMaxExpect,
                longitudeMinExpect,
                longitudeMaxExpect,
                userId
            );
        }
    }
}