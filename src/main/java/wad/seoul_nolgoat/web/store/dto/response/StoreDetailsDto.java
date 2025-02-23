package wad.seoul_nolgoat.web.store.dto.response;

import wad.seoul_nolgoat.domain.store.StoreType;
import wad.seoul_nolgoat.web.review.dto.response.ReviewDetailsForStoreDto;

import java.util.List;

public record StoreDetailsDto(
        Long storeId,
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
        double nolgoatAverageGrade,
        String placeUrl,
        List<ReviewDetailsForStoreDto> reviews
) {
}