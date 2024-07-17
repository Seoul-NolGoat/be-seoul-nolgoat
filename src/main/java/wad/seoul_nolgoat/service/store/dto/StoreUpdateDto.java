package wad.seoul_nolgoat.service.store.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wad.seoul_nolgoat.domain.store.StoreType;

@Getter
@RequiredArgsConstructor
public class StoreUpdateDto {

    private final Long id;
    private final StoreType storeType;
    private final String name;
    private final String category;
    private final String managementNumber;
    private final String phoneNumber;
    private final String lotAddress;
    private final String roadAddress;
    private final double latitude;
    private final double longitude;
    private final double kakaoAverageGrade;
    private final String placeUrl;
}
