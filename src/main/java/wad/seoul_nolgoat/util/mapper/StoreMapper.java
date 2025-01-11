package wad.seoul_nolgoat.util.mapper;

import wad.seoul_nolgoat.domain.review.Review;
import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.service.search.dto.StoreForDistanceSortDto;
import wad.seoul_nolgoat.service.search.dto.StoreForGradeSortDto;
import wad.seoul_nolgoat.web.store.dto.response.StoreDetailsDto;
import wad.seoul_nolgoat.web.store.dto.response.StoreForCombinationDto;
import wad.seoul_nolgoat.web.store.dto.response.StoreListDto;

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

    public static StoreForCombinationDto toStoreForCombinationDto(StoreForDistanceSortDto storeForDistanceSortDto) {
        return new StoreForCombinationDto(
                storeForDistanceSortDto.getId(),
                storeForDistanceSortDto.getName(),
                storeForDistanceSortDto.getCoordinate(),
                storeForDistanceSortDto.getKakaoAverageGrade(),
                storeForDistanceSortDto.getNolgoatAverageGrade()
        );
    }

    public static StoreForCombinationDto toStoreForCombinationDto(StoreForGradeSortDto storeForGradeSortDto) {
        return new StoreForCombinationDto(
                storeForGradeSortDto.getId(),
                storeForGradeSortDto.getName(),
                storeForGradeSortDto.getCoordinate(),
                storeForGradeSortDto.getKakaoAverageGrade(),
                storeForGradeSortDto.getNolgoatAverageGrade()
        );
    }

    public static StoreListDto toStoreListDto(Store store) {
        return new StoreListDto(
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
