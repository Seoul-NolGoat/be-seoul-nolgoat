package wad.seoul_nolgoat.web.party.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record PartyUpdateDto(
        @NotBlank @Size(max = 40, message = "제목은 40자 이내여야 합니다.")
        String title,
        @NotBlank @Size(max = 300, message = "내용은 300자 이내여야 합니다.")
        String content,
        @Min(value = 2, message = "참여 가능 인원은 본인 제외 1명 이상이어야 합니다.") @Max(value = 30, message = "참여 가능 인원은 30명을 초과할 수 없습니다.")
        int maxCapacity,
        @NotNull @FutureOrPresent(message = "모임 일은 현재 시간 이후여야 합니다.")
        LocalDateTime meetingDate,
        @NotBlank
        String administrativeDistrict
) {
}