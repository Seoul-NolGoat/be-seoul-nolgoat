package wad.seoul_nolgoat.web.search.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CoordinateDto {

    private final double latitude;
    private final double longitude;
}
