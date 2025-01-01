package wad.seoul_nolgoat.web.party.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PartySaveDto {

    @NotBlank
    @Size(max = 25, message = "제목은 25자 이내여야 합니다.")
    private final String title;

    @NotBlank
    @Size(max = 300, message = "내용은 300자 이내여야 합니다.")
    private final String content;

    @Min(value = 2, message = "참여 가능 인원은 본인 제외 1명 이상이어야 합니다.")
    @Max(value = 30, message = "참여 가능 인원은 30명을 초과할 수 없습니다.")
    private final int maxCapacity;

    @NotNull
    @Future(message = "마감시간은 현재 시간 이후여야 합니다.")
    private final LocalDateTime deadline;

    @NotBlank
    private final String administrativeDistrict;
}
