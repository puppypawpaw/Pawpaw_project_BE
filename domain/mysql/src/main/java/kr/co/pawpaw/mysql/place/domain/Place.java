package kr.co.pawpaw.mysql.place.domain;

import kr.co.pawpaw.mysql.common.BaseTimeEntity;
import kr.co.pawpaw.mysql.common.domain.Position;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT")
    private final List<String> placeImageUrls = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String openHours;

    @Embedded
    private final ReviewInfo reviewInfo = new ReviewInfo();

    @Builder
    private Place(
        final Collection<String> placeImageUrls,
        final PlaceType placeType,
        final String name,
        final Position position,
        final String openHours
    ) {
        this.placeImageUrls.addAll(placeImageUrls);
        this.placeType = placeType;
        this.name = name;
        this.position = position;
        this.openHours = openHours;
    }
}
