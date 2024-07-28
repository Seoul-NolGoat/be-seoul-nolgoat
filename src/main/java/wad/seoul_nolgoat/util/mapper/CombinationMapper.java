package wad.seoul_nolgoat.util.mapper;

import wad.seoul_nolgoat.service.search.dto.DistanceSortCombinationDto;
import wad.seoul_nolgoat.service.search.dto.GradeSortCombinationDto;
import wad.seoul_nolgoat.web.search.dto.response.CombinationDto;

public class CombinationMapper {

    public static CombinationDto toCombinationDto(DistanceSortCombinationDto distanceSortCombinationDto) {
        return new CombinationDto(
                StoreMapper.toStoreForCombinationDto(distanceSortCombinationDto.getFirstStore()),
                StoreMapper.toStoreForCombinationDto(distanceSortCombinationDto.getSecondStore()),
                StoreMapper.toStoreForCombinationDto(distanceSortCombinationDto.getThirdStore())
        );
    }

    public static CombinationDto toCombinationDto(GradeSortCombinationDto gradeSortCombinationDto) {
        return new CombinationDto(
                StoreMapper.toStoreForCombinationDto(gradeSortCombinationDto.getFirstStore()),
                StoreMapper.toStoreForCombinationDto(gradeSortCombinationDto.getSecondStore()),
                StoreMapper.toStoreForCombinationDto(gradeSortCombinationDto.getThirdStore())
        );
    }
}
