package wad.seoul_nolgoat.web.kakaoMap;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wad.seoul_nolgoat.service.kakaoMap.KakaoMapService;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/api/kakao/map")
@RestController
public class KakaoMapController {

    private final KakaoMapService kakaoMapService;

    @GetMapping("/road-address")
    public ResponseEntity<String> getRoadAddress(@ModelAttribute CoordinateDto coordinate) {
        Optional<String> optionalAddress = kakaoMapService.fetchRoadAddress(coordinate);
        if (optionalAddress.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity
                .ok(optionalAddress.get());
    }

    @GetMapping("/lot-address")
    public ResponseEntity<String> getLotAddress(@ModelAttribute CoordinateDto coordinate) {
        Optional<String> optionalAddress = kakaoMapService.fetchLotAddress(coordinate);
        if (optionalAddress.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity
                .ok(optionalAddress.get());
    }

    @GetMapping("/coordinate")
    public ResponseEntity<CoordinateDto> getCoordinate(@RequestParam String address) {
        Optional<CoordinateDto> optionalCoordinate = kakaoMapService.fetchCoordinate(address);
        if (optionalCoordinate.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity
                .ok(optionalCoordinate.get());
    }
}
