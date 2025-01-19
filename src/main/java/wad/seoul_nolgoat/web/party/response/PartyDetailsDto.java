package wad.seoul_nolgoat.web.party.response;

import lombok.Getter;
import wad.seoul_nolgoat.domain.party.AdministrativeDistrict;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PartyDetailsDto {

    private final Long id;
    private final String title;
    private final String content;
    private final int maxCapacity;
    private final LocalDateTime meetingDate;
    private final boolean isClosed;
    private final String district;
    private final int currentCount;
    private final Long hostId;
    private final String hostNickname;
    private final String hostProfileImage;
    private final List<ParticipantDto> participants;

    public PartyDetailsDto(
            Long id,
            String title,
            String content,
            int maxCapacity,
            LocalDateTime meetingDate,
            boolean isClosed,
            AdministrativeDistrict district,
            int currentCount,
            Long hostId,
            String hostNickname,
            String hostProfileImage,
            List<ParticipantDto> participants
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.maxCapacity = maxCapacity;
        this.meetingDate = meetingDate;
        this.isClosed = isClosed;
        this.district = district.getDisplayName();
        this.currentCount = currentCount;
        this.hostId = hostId;
        this.hostNickname = hostNickname;
        this.hostProfileImage = hostProfileImage;
        this.participants = participants;
    }
}
