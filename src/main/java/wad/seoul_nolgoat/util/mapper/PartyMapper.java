package wad.seoul_nolgoat.util.mapper;

import wad.seoul_nolgoat.domain.party.Party;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.web.party.request.PartySaveDto;

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
}
