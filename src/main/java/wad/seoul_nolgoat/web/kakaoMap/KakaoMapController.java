package wad.seoul_nolgoat.web.kakaoMap;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wad.seoul_nolgoat.service.kakaoMap.KakaoMapService;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

import java.util.Optional;

@Tag(name = "카카오맵")
@RequiredArgsConstructor
@RequestMapping("/api/kakao/map")
@RestController
public class KakaoMapController {

    private final KakaoMapService kakaoMapService;

    @Operation(summary = "유저 좌표를 도로명 주소로 변경")
    @GetMapping("/road-address")
    public ResponseEntity<String> getRoadAddress(@ModelAttribute CoordinateDto coordinate) {
        Optional<String> optionalAddress = kakaoMapService.fetchRoadAddress(coordinate);
        return optionalAddress.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());
    }

    @Operation(summary = "유저 좌표를 지번 주소로 변경")
    @GetMapping("/lot-address")
    public ResponseEntity<String> getLotAddress(@ModelAttribute CoordinateDto coordinate) {
        Optional<String> optionalAddress = kakaoMapService.fetchLotAddress(coordinate);
        return optionalAddress.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());
    }

    @Operation(summary = "주소에 해당하는 좌표 반환")
    @GetMapping("/coordinate")
    public ResponseEntity<CoordinateDto> getCoordinate(@RequestParam String address) {
        Optional<CoordinateDto> optionalCoordinate = kakaoMapService.fetchCoordinate(address);
        return optionalCoordinate.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());
    }
}
