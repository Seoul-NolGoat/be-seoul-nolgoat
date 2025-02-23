package wad.seoul_nolgoat.service.store.dto;

import wad.seoul_nolgoat.domain.store.StoreType;

public record StoreUpdateDto(
        Long id,
        StoreType storeType,
        String name,
        String category,
        String managementNumber,
        String phoneNumber,
        String lotAddress,
        String roadAddress,
        double latitude,
        double longitude,
        double kakaoAverageGrade,
        String placeUrl
) {
}