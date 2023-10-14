package kr.co.pawpaw.api.dto.user;

import kr.co.pawpaw.api.dto.position.PositionRequest;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateUserPositionRequest {
    private PositionRequest position;
}
