package kr.co.pawpaw.mysql.place.domain;

import kr.co.pawpaw.mysql.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceImageUrl extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;

    @Column(columnDefinition = "TEXT")
    private String url;

    @Builder
    public PlaceImageUrl(final Place place, final String url) {
        this.place = place;
        this.url = url;
    }
}
