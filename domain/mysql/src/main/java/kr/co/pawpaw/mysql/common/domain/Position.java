package kr.co.pawpaw.mysql.common.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Position {
    private Double latitude;
    private Double longitude;
    private String address;

    public boolean isInside(
        final double latMin,
        final double latMax,
        final double longMin,
        final double longMax
    ) {
        return latMin < latitude && latitude < latMax && longMin < longitude && longitude < longMax;
    }
}
