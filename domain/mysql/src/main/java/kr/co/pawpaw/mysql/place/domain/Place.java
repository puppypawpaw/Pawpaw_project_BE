package kr.co.pawpaw.mysql.place.domain;

import kr.co.pawpaw.mysql.common.BaseTimeEntity;
import kr.co.pawpaw.mysql.common.domain.Position;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private PlaceType placeType;

    @Column(columnDefinition = "VARCHAR(2048) NOT NULL, FULLTEXT INDEX name_fulltext (name) WITH PARSER ngram")
    private String name;

    @Embedded
    private Position position;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "open", column = @Column(name = "mon_open")),
        @AttributeOverride(name = "close", column = @Column(name = "mon_close")),
        @AttributeOverride(name = "lastOrder", column = @Column(name = "mon_last_order"))
    })
    private DayOpenHour monOpenHour;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "open", column = @Column(name = "tue_open")),
        @AttributeOverride(name = "close", column = @Column(name = "tue_close")),
        @AttributeOverride(name = "lastOrder", column = @Column(name = "tue_last_order"))
    })
    private DayOpenHour tueOpenHour;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "open", column = @Column(name = "wed_open")),
        @AttributeOverride(name = "close", column = @Column(name = "wed_close")),
        @AttributeOverride(name = "lastOrder", column = @Column(name = "wed_last_order"))
    })
    private DayOpenHour wedOpenHour;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "open", column = @Column(name = "thu_open")),
        @AttributeOverride(name = "close", column = @Column(name = "thu_close")),
        @AttributeOverride(name = "lastOrder", column = @Column(name = "thu_last_order"))
    })
    private DayOpenHour thuOpenHour;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "open", column = @Column(name = "fri_open")),
        @AttributeOverride(name = "close", column = @Column(name = "fri_close")),
        @AttributeOverride(name = "lastOrder", column = @Column(name = "fri_last_order"))
    })
    private DayOpenHour friOpenHour;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "open", column = @Column(name = "sat_open")),
        @AttributeOverride(name = "close", column = @Column(name = "sat_close")),
        @AttributeOverride(name = "lastOrder", column = @Column(name = "sat_last_order"))
    })
    private DayOpenHour satOpenHour;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "open", column = @Column(name = "sun_open")),
        @AttributeOverride(name = "close", column = @Column(name = "sun_close")),
        @AttributeOverride(name = "lastOrder", column = @Column(name = "sun_last_order"))
    })
    private DayOpenHour sunOpenHour;

    @Embedded
    private final ReviewInfo reviewInfo = new ReviewInfo();

    @Builder
    private Place(
        final PlaceType placeType,
        final String name,
        final Position position,
        final DayOpenHour monOpenHour,
        final DayOpenHour tueOpenHour,
        final DayOpenHour wedOpenHour,
        final DayOpenHour thuOpenHour,
        final DayOpenHour friOpenHour,
        final DayOpenHour satOpenHour,
        final DayOpenHour sunOpenHour
    ) {
        this.placeType = placeType;
        this.name = name;
        this.position = position;
        this.monOpenHour = monOpenHour;
        this.tueOpenHour = tueOpenHour;
        this.wedOpenHour = wedOpenHour;
        this.thuOpenHour = thuOpenHour;
        this.friOpenHour = friOpenHour;
        this.satOpenHour = satOpenHour;
        this.sunOpenHour = sunOpenHour;
    }
}
