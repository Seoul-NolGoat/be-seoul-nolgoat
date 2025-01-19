package wad.seoul_nolgoat.util.mapper;

import wad.seoul_nolgoat.domain.party.Party;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.web.party.request.PartySaveDto;

public class PartyMapper {

    public static Party toEntity(PartySaveDto partySaveDto, User user) {
        return new Party(
                partySaveDto.getTitle(),
                partySaveDto.getContent(),
                partySaveDto.getMaxCapacity(),
                partySaveDto.getDeadline(),
                partySaveDto.getAdministrativeDistrict(),
                user
        );
    }
}
