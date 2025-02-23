package wad.seoul_nolgoat.util.mapper;

import wad.seoul_nolgoat.domain.party.AdministrativeDistrict;
import wad.seoul_nolgoat.domain.party.Party;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.web.party.dto.request.PartySaveDto;

public class PartyMapper {

    public static Party toEntity(PartySaveDto partySaveDto, User user) {
        return new Party(
                partySaveDto.title(),
                partySaveDto.content(),
                partySaveDto.maxCapacity(),
                partySaveDto.meetingDate(),
                AdministrativeDistrict.fromString(partySaveDto.administrativeDistrict()),
                user
        );
    }
}
