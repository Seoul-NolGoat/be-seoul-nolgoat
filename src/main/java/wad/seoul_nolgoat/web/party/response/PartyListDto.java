package wad.seoul_nolgoat.web.party.response;

import lombok.Getter;
import wad.seoul_nolgoat.domain.party.AdministrativeDistrict;

import java.time.LocalDateTime;

@Getter
public class PartyListDto {

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

    public PartyListDto(
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
}
