package wad.seoul_nolgoat.web.party.response;

import lombok.Getter;
import wad.seoul_nolgoat.domain.party.AdministrativeDistrict;

import java.time.LocalDateTime;

@Getter
public class HostedPartyListDto {

    private final Long partyId;
    private final String title;
    private final int maxCapacity;
    private final LocalDateTime deadline;
    private final boolean isClosed;
    private final String district;
    private final int currentCount;

    public HostedPartyListDto(
            Long partyId,
            String title,
            int maxCapacity,
            LocalDateTime deadline,
            boolean isClosed,
            AdministrativeDistrict district,
            int currentCount
    ) {
        this.partyId = partyId;
        this.title = title;
        this.maxCapacity = maxCapacity;
        this.deadline = deadline;
        this.isClosed = isClosed;
        this.district = district.getDisplayName();
        this.currentCount = currentCount;
    }
}
