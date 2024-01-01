package kr.co.pawpaw.mysql.place.enums;

import lombok.Getter;

@Getter
public enum PlaceTag {
    SCENIC(0.5, 1.0),
    QUIET(0.5, 1.0),
    COMFORTABLE(0.5, 1.0),
    ACCESSIBLE(0.5, 1.0),
    CLEAN(0.5, 1.0),
    SAFE(0.5, 1.0),
    MOST_SAVED(0.0, 0.3);

    private final double visibleConditionLowerPercentLimit;
    private final double visibleConditionUpperPercentLimit;

    PlaceTag(
        double visibleConditionLowerPercentLimit,
        double visibleConditionUpperPercentLimit
    ) {
        this.visibleConditionLowerPercentLimit = visibleConditionLowerPercentLimit;
        this.visibleConditionUpperPercentLimit = visibleConditionUpperPercentLimit;
    }

    public boolean isPlaceTagVisible(double placeTagPercentage) {
        return visibleConditionLowerPercentLimit <= placeTagPercentage
            && placeTagPercentage <= visibleConditionUpperPercentLimit;
    }
}