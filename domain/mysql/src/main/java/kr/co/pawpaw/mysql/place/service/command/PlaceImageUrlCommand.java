package kr.co.pawpaw.mysql.place.service.command;

import kr.co.pawpaw.mysql.place.domain.PlaceImageUrl;
import kr.co.pawpaw.mysql.place.repository.PlaceImageUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceImageUrlCommand {
    private final PlaceImageUrlRepository placeImageUrlRepository;

    public List<PlaceImageUrl> saveAll(final List<PlaceImageUrl> placeImageUrlList) {
        return placeImageUrlRepository.saveAll(placeImageUrlList);
    }
}
