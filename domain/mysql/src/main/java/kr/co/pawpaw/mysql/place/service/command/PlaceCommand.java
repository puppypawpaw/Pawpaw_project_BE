package kr.co.pawpaw.mysql.place.service.command;

import kr.co.pawpaw.mysql.place.domain.Place;
import kr.co.pawpaw.mysql.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceCommand {
    private final PlaceRepository placeRepository;

    public List<Place> saveAll(final List<Place> placeList) {
        return placeRepository.saveAll(placeList);
    }
}
