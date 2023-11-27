package kr.co.pawpaw.mysql.place.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DayOpenHour {
    private String open;
    private String close;
    private String lastOrder;
}
