package wad.seoul_nolgoat.util.mapper;

import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.service.search.SearchService;
import wad.seoul_nolgoat.service.search.dto.DistanceSortCombinationDto;
import wad.seoul_nolgoat.service.search.dto.GradeSortCombinationDto;
import wad.seoul_nolgoat.web.search.dto.response.CombinationDto;

import static wad.seoul_nolgoat.exception.ErrorCode.INVALID_GATHERING_ROUND;

public class CombinationMapper {

    public static CombinationDto toCombinationDto(DistanceSortCombinationDto distanceSortCombinationDto) {
        if (distanceSortCombinationDto.getTotalRounds() == SearchService.THREE_ROUND) {
            return new CombinationDto(
                    StoreMapper.toStoreForCombinationDto(distanceSortCombinationDto.getFirstStore()),
                    StoreMapper.toStoreForCombinationDto(distanceSortCombinationDto.getSecondStore()),
                    StoreMapper.toStoreForCombinationDto(distanceSortCombinationDto.getThirdStore()),
                    distanceSortCombinationDto.getWalkRouteInfoDto()
            );
        }
        if (distanceSortCombinationDto.getTotalRounds() == SearchService.TWO_ROUND) {
            return new CombinationDto(
                    StoreMapper.toStoreForCombinationDto(distanceSortCombinationDto.getFirstStore()),
                    StoreMapper.toStoreForCombinationDto(distanceSortCombinationDto.getSecondStore()),
                    distanceSortCombinationDto.getWalkRouteInfoDto()
            );
        }
        if (distanceSortCombinationDto.getTotalRounds() == SearchService.ONE_ROUND) {
            return new CombinationDto(
                    StoreMapper.toStoreForCombinationDto(distanceSortCombinationDto.getFirstStore()),
                    distanceSortCombinationDto.getWalkRouteInfoDto()
            );
        }
        throw new ApiException(INVALID_GATHERING_ROUND);
    }

    public static CombinationDto toCombinationDto(GradeSortCombinationDto gradeSortCombinationDto) {
        if (gradeSortCombinationDto.getTotalRounds() == SearchService.THREE_ROUND) {
            return new CombinationDto(
                    StoreMapper.toStoreForCombinationDto(gradeSortCombinationDto.getFirstStore()),
                    StoreMapper.toStoreForCombinationDto(gradeSortCombinationDto.getSecondStore()),
                    StoreMapper.toStoreForCombinationDto(gradeSortCombinationDto.getThirdStore())
            );
        }
        if (gradeSortCombinationDto.getTotalRounds() == SearchService.TWO_ROUND) {
            return new CombinationDto(
                    StoreMapper.toStoreForCombinationDto(gradeSortCombinationDto.getFirstStore()),
                    StoreMapper.toStoreForCombinationDto(gradeSortCombinationDto.getSecondStore())
            );
        }
        if (gradeSortCombinationDto.getTotalRounds() == SearchService.ONE_ROUND) {
            return new CombinationDto(
                    StoreMapper.toStoreForCombinationDto(gradeSortCombinationDto.getFirstStore())
            );
        }
        throw new ApiException(INVALID_GATHERING_ROUND);
    }
}
