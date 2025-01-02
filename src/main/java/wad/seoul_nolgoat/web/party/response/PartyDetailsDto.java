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
    private final String imageUrl;
    private final int maxCapacity;
    private final LocalDateTime deadline;
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
            String imageUrl,
            int maxCapacity,
            LocalDateTime deadline,
            boolean isClosed,
            AdministrativeDistrict district,
            Long currentCount,
            Long hostId,
            String hostNickname,
            String hostProfileImage,
            List<ParticipantDto> participants
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.maxCapacity = maxCapacity;
        this.deadline = deadline;
        this.isClosed = isClosed;
        this.district = district.getDisplayName();
        this.currentCount = currentCount.intValue() + 1;
        this.hostId = hostId;
        this.hostNickname = hostNickname;
        this.hostProfileImage = hostProfileImage;
        this.participants = participants;
    }
}
