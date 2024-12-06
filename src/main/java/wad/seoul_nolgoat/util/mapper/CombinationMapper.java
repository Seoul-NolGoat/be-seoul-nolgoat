package wad.seoul_nolgoat.util.mapper;

import wad.seoul_nolgoat.service.search.SearchService;
import wad.seoul_nolgoat.service.search.dto.DistanceSortCombinationDto;
import wad.seoul_nolgoat.service.search.dto.GradeSortCombinationDto;
import wad.seoul_nolgoat.service.tMap.TMapService;
import wad.seoul_nolgoat.service.tMap.dto.WalkRouteInfoDto;
import wad.seoul_nolgoat.web.search.dto.response.CombinationDto;

public class CombinationMapper {

    public static CombinationDto toCombinationDto(DistanceSortCombinationDto distanceSortCombinationDto) {
        WalkRouteInfoDto walkRouteInfoDto = distanceSortCombinationDto.getWalkRouteInfoDto();
        if (walkRouteInfoDto == null) {
            walkRouteInfoDto = new WalkRouteInfoDto(distanceSortCombinationDto.getTotalDistance(), TMapService.INVALID_TIME);
        }
        if (distanceSortCombinationDto.getTotalRounds() == SearchService.THREE_ROUND) {
            return new CombinationDto(
                    StoreMapper.toStoreForCombinationDto(distanceSortCombinationDto.getFirstStore()),
                    StoreMapper.toStoreForCombinationDto(distanceSortCombinationDto.getSecondStore()),
                    StoreMapper.toStoreForCombinationDto(distanceSortCombinationDto.getThirdStore()),
                    walkRouteInfoDto
            );
        }
        if (distanceSortCombinationDto.getTotalRounds() == SearchService.TWO_ROUND) {
            return new CombinationDto(
                    StoreMapper.toStoreForCombinationDto(distanceSortCombinationDto.getFirstStore()),
                    StoreMapper.toStoreForCombinationDto(distanceSortCombinationDto.getSecondStore()),
                    walkRouteInfoDto
            );
        }

        // 1차인 경우
        return new CombinationDto(
                StoreMapper.toStoreForCombinationDto(distanceSortCombinationDto.getFirstStore()),
                walkRouteInfoDto
        );
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

        // 1차인 경우
        return new CombinationDto(
                StoreMapper.toStoreForCombinationDto(gradeSortCombinationDto.getFirstStore())
        );
    }
}
