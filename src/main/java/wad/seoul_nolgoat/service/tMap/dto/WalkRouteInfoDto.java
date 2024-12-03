package wad.seoul_nolgoat.service.tMap.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WalkRouteInfoDto {

    private final int tMapTotalDistance;
    private final int totalTime;
}
