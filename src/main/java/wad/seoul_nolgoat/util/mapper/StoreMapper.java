package wad.seoul_nolgoat.util.mapper;

import wad.seoul_nolgoat.domain.review.Review;
import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.service.search.dto.StoreForDistanceSortDto;
import wad.seoul_nolgoat.service.search.dto.StoreForGradeSortDto;
import wad.seoul_nolgoat.web.store.dto.response.StoreDetailsDto;
import wad.seoul_nolgoat.web.store.dto.response.StoreDetailsForCombinationDto;
import wad.seoul_nolgoat.web.store.dto.response.StoreDetailsForListDto;

import java.util.List;
import java.util.Objects;

public class StoreMapper {

    public static StoreDetailsDto toStoreDetailsDto(Store store, List<Review> reviews) {
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
                reviews.stream()
                        .filter(Objects::nonNull) // review가 존재할 때만 적용
                        .map(ReviewMapper::toReviewDetailsForStoreDto)
                        .toList()
        );
    }

    public static StoreDetailsForCombinationDto toStoreForCombinationDto(StoreForDistanceSortDto storeForDistanceSortDto) {
        return new StoreDetailsForCombinationDto(
                storeForDistanceSortDto.getId(),
                storeForDistanceSortDto.getName(),
                storeForDistanceSortDto.getCoordinate(),
                storeForDistanceSortDto.getKakaoAverageGrade(),
                storeForDistanceSortDto.getNolgoatAverageGrade()
        );
    }

    public static StoreDetailsForCombinationDto toStoreForCombinationDto(StoreForGradeSortDto storeForGradeSortDto) {
        return new StoreDetailsForCombinationDto(
                storeForGradeSortDto.getId(),
                storeForGradeSortDto.getName(),
                storeForGradeSortDto.getCoordinate(),
                storeForGradeSortDto.getKakaoAverageGrade(),
                storeForGradeSortDto.getNolgoatAverageGrade()
        );
    }

    public static StoreDetailsForListDto toStoreListDto(Store store) {
        return new StoreDetailsForListDto(
                store.getId(),
                store.getName(),
                store.getCategory(),
                store.getPhoneNumber(),
                store.getRoadAddress(),
                store.getKakaoAverageGrade(),
                store.getNolgoatAverageGrade()
        );
    }
}
