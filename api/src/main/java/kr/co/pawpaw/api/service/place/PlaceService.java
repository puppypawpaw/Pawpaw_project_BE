package kr.co.pawpaw.api.service.place;

import kr.co.pawpaw.api.dto.place.CreatePlaceRequest;
import kr.co.pawpaw.mysql.place.domain.PlaceType;
import kr.co.pawpaw.mysql.place.dto.PlaceResponse;
import kr.co.pawpaw.mysql.place.service.command.PlaceCommand;
import kr.co.pawpaw.mysql.place.service.query.PlaceQuery;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceCommand placeCommand;

    public void createPlaceAll(final List<CreatePlaceRequest> requestList) {
        placeCommand.saveAll(requestList.stream()
            .map(CreatePlaceRequest::toPlace)
            .collect(Collectors.toList()));
    }
}
