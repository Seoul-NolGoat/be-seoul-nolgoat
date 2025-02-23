package wad.seoul_nolgoat.web.party.dto.response;

import lombok.Getter;
import wad.seoul_nolgoat.domain.party.AdministrativeDistrict;
import wad.seoul_nolgoat.domain.party.Party;

import java.time.LocalDateTime;

@Getter
public class PartyDetailsForUserDto {

    private final Long partyId;
    private final String title;
    private final int maxCapacity;
    private final LocalDateTime meetingDate;
    private final boolean isClosed;
    private final String district;
    private final int currentCount;
    private final LocalDateTime createdDate;

    private PartyDetailsForUserDto(
            Long partyId,
            String title,
            int maxCapacity,
            LocalDateTime meetingDate,
            boolean isClosed,
            AdministrativeDistrict district,
            int currentCount,
            LocalDateTime createdDate
    ) {
        this.partyId = partyId;
        this.title = title;
        this.maxCapacity = maxCapacity;
        this.meetingDate = meetingDate;
        this.isClosed = isClosed;
        this.district = district.getDisplayName();
        this.currentCount = currentCount;
        this.createdDate = createdDate;
    }

    public static PartyDetailsForUserDto from(Party party) {
        return new PartyDetailsForUserDto(
                party.getId(),
                party.getTitle(),
                party.getMaxCapacity(),
                party.getMeetingDate(),
                party.isClosed(),
                party.getAdministrativeDistrict(),
                party.getCurrentCount(),
                party.getCreatedDate()
        );
    }
}
