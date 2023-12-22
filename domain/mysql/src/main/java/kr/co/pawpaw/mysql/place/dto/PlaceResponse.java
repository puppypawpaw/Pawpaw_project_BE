package kr.co.pawpaw.mysql.place.dto;

import kr.co.pawpaw.mysql.common.dto.PositionResponse;
import kr.co.pawpaw.mysql.place.enums.PlaceTag;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public class PlaceResponse extends PlaceQueryDSLResponse {
    private final List<PlaceTag> placeTagList;

    public PlaceResponse(
        final Long id,
        final Set<String> imageUrlList,
        final String name,
        final PositionResponse position,
        final String monOpen,
        final String monClose,
        final String monLastOrder,
        final String tueOpen,
        final String tueClose,
        final String tueLastOrder,
        final String wedOpen,
        final String wedClose,
        final String wedLastOrder,
        final String thuOpen,
        final String thuClose,
        final String thuLastOrder,
        final String friOpen,
        final String friClose,
        final String friLastOrder,
        final String satOpen,
        final String satClose,
        final String satLastOrder,
        final String sunOpen,
        final String sunClose,
        final String sunLastOrder,
        final boolean bookmarked,
        final Double score,
        final Double scenicRatio,
        final Double quietRatio,
        final Double comfortableRatio,
        final Double accessibleRatio,
        final Double cleanRatio,
        final Double safeRatio,
        final List<PlaceTag> placeTagList
    ) {
        super(
            id,
            imageUrlList,
            name,
            position,
            monOpen,
            monClose,
            monLastOrder,
            tueOpen,
            tueClose,
            tueLastOrder,
            wedOpen,
            wedClose,
            wedLastOrder,
            thuOpen,
            thuClose,
            thuLastOrder,
            friOpen,
            friClose,
            friLastOrder,
            satOpen,
            satClose,
            satLastOrder,
            sunOpen,
            sunClose,
            sunLastOrder,
            bookmarked,
            score,
            scenicRatio,
            quietRatio,
            comfortableRatio,
            accessibleRatio,
            cleanRatio,
            safeRatio
        );
        this.placeTagList = placeTagList;
    }

    public PlaceResponse(
        final PlaceQueryDSLResponse placeQueryDSLResponse,
        final List<PlaceTag> placeTagList
    ) {
        super(
            placeQueryDSLResponse.getId(),
            placeQueryDSLResponse.getImageUrlList(),
            placeQueryDSLResponse.getName(),
            placeQueryDSLResponse.getPosition(),
            placeQueryDSLResponse.getMonOpen(),
            placeQueryDSLResponse.getMonClose(),
            placeQueryDSLResponse.getMonLastOrder(),
            placeQueryDSLResponse.getTueOpen(),
            placeQueryDSLResponse.getTueClose(),
            placeQueryDSLResponse.getTueLastOrder(),
            placeQueryDSLResponse.getWedOpen(),
            placeQueryDSLResponse.getWedClose(),
            placeQueryDSLResponse.getWedLastOrder(),
            placeQueryDSLResponse.getThuOpen(),
            placeQueryDSLResponse.getThuClose(),
            placeQueryDSLResponse.getThuLastOrder(),
            placeQueryDSLResponse.getFriOpen(),
            placeQueryDSLResponse.getFriClose(),
            placeQueryDSLResponse.getFriLastOrder(),
            placeQueryDSLResponse.getSatOpen(),
            placeQueryDSLResponse.getSatClose(),
            placeQueryDSLResponse.getSatLastOrder(),
            placeQueryDSLResponse.getSunOpen(),
            placeQueryDSLResponse.getSunClose(),
            placeQueryDSLResponse.getSunLastOrder(),
            placeQueryDSLResponse.isBookmarked(),
            placeQueryDSLResponse.getScore(),
            placeQueryDSLResponse.getScenicRatio(),
            placeQueryDSLResponse.getQuietRatio(),
            placeQueryDSLResponse.getComfortableRatio(),
            placeQueryDSLResponse.getAccessibleRatio(),
            placeQueryDSLResponse.getCleanRatio(),
            placeQueryDSLResponse.getSafeRatio()
        );
        this.placeTagList = placeTagList;
    }
}
