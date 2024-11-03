package wad.seoul_nolgoat.util.mapper;

import wad.seoul_nolgoat.domain.party.Party;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.web.party.request.PartySaveDto;
import wad.seoul_nolgoat.web.party.response.PartyDetailsDto;

public class PartyMapper {

    public static Party toEntity(
            PartySaveDto partySaveDto,
            User user,
            String imageUrl
    ) {
        return new Party(
                partySaveDto.getTitle(),
                imageUrl,
                partySaveDto.getMaxCapacity(),
                partySaveDto.getDeadline(),
                user
        );
    }

    public static PartyDetailsDto toPartyDetailsDto(Party party, int currentCount) {
        return new PartyDetailsDto(
                party.getId(),
                party.getTitle(),
                party.getImageUrl(),
                party.getMaxCapacity(),
                party.getDeadline(),
                party.isClosed(),
                currentCount
        );
    }
}
