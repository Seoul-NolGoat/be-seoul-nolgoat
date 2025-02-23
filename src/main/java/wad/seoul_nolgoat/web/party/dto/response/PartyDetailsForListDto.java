package wad.seoul_nolgoat.web.party.dto.response;

import lombok.Getter;
import wad.seoul_nolgoat.domain.party.AdministrativeDistrict;
import wad.seoul_nolgoat.domain.party.Party;

import java.time.LocalDateTime;

@Getter
public class PartyDetailsForListDto {

    private final Long partyId;
    private final String title;
    private final int maxCapacity;
    private final LocalDateTime meetingDate;
    private final boolean isClosed;
    private final String district;
    private final int currentCount;
    private final LocalDateTime createdDate;
    private final Long hostId;
    private final String hostNickname;
    private final String hostProfileImage;

    private PartyDetailsForListDto(
            Long partyId,
            String title,
            int maxCapacity,
            LocalDateTime meetingDate,
            boolean isClosed,
            AdministrativeDistrict district,
            int currentCount,
            LocalDateTime createdDate,
            Long hostId,
            String hostNickname,
            String hostProfileImage
    ) {
        this.partyId = partyId;
        this.title = title;
        this.maxCapacity = maxCapacity;
        this.meetingDate = meetingDate;
        this.isClosed = isClosed;
        this.district = district.getDisplayName();
        this.currentCount = currentCount;
        this.createdDate = createdDate;
        this.hostId = hostId;
        this.hostNickname = hostNickname;
        this.hostProfileImage = hostProfileImage;
    }

    public static PartyDetailsForListDto from(Party party) {
        return new PartyDetailsForListDto(
                party.getId(),
                party.getTitle(),
                party.getMaxCapacity(),
                party.getMeetingDate(),
                party.isClosed(),
                party.getAdministrativeDistrict(),
                party.getCurrentCount(),
                party.getCreatedDate(),
                party.getHost().getId(),
                party.getHost().getNickname(),
                party.getHost().getProfileImage()
        );
    }
}
