package wad.seoul_nolgoat.web.party.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PartyDetailsDto {

    private final Long id;
    private final String title;
    private final String imageUrl;
    private final int maxCapacity;
    private final LocalDateTime deadline;
    private final boolean isClosed;
    private final int currentCount;
}
