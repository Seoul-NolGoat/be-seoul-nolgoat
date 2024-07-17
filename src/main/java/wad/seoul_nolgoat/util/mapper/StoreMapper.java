package wad.seoul_nolgoat.util.mapper;

import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.web.store.dto.response.StoreDetailsDto;

public class StoreMapper {

    public static StoreDetailsDto toStoreDetailsDto(Store store) {
        return new StoreDetailsDto(
                store.getId(),
                store.getStoreType(),
                store.getName(),
                store.getCategory(),
                store.getManagementNumber(),
                store.getPhoneNumber(),
                store.getLotAddress(),
                store.getRoadAddress(),
                store.getLatitude(),
                store.getLongitude(),
                store.getKakaoAverageGrade(),
                store.getNolgoatAverageGrade(),
                store.getPlaceUrl(),
                store.getReviews().stream()
                        .map(ReviewMapper::toReviewDetailsForStoreDto)
                        .toList()
        );
    }
}
