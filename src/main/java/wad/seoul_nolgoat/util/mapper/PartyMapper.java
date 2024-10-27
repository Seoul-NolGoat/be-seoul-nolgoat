package wad.seoul_nolgoat.util.mapper;

import wad.seoul_nolgoat.domain.party.Party;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.web.party.request.PartySaveDto;

import java.util.Optional;

public class PartyMapper {

    public static Party toEntity(
            PartySaveDto partySaveDto,
            User user,
            Optional<String> optionalImageUrl
    ) {
        if (optionalImageUrl.isPresent()) {
            return new Party(
                    partySaveDto.getTitle(),
                    optionalImageUrl.get(),
                    partySaveDto.getMaxCapacity(),
                    partySaveDto.getDeadline(),
                    user

            );
        }
        return new Party(
                partySaveDto.getTitle(),
                partySaveDto.getMaxCapacity(),
                partySaveDto.getDeadline(),
                user
        );
    }
}
