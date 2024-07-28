package wad.seoul_nolgoat.util.mapper;

import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.service.search.dto.StoreForDistanceSortDto;
import wad.seoul_nolgoat.service.search.dto.StoreForGradeSortDto;
import wad.seoul_nolgoat.web.store.dto.response.StoreDetailsDto;
import wad.seoul_nolgoat.web.store.dto.response.StoreForCombinationDto;

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

    public static StoreForCombinationDto toStoreForCombinationDto(StoreForDistanceSortDto storeForDistanceSortDto) {
        return new StoreForCombinationDto(
                storeForDistanceSortDto.getId(),
                storeForDistanceSortDto.getName(),
                storeForDistanceSortDto.getCoordinate()
        );
    }

    public static StoreForCombinationDto toStoreForCombinationDto(StoreForGradeSortDto storeForGradeSortDto) {
        return new StoreForCombinationDto(
                storeForGradeSortDto.getId(),
                storeForGradeSortDto.getName(),
                storeForGradeSortDto.getCoordinate()
        );
    }
}
