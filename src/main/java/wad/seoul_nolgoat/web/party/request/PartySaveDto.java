package wad.seoul_nolgoat.web.party.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PartySaveDto {

    private final String title;
    private final int maxCapacity;
    private final LocalDateTime deadline;

    // 지역 추가
    // private Enum? location;
}
