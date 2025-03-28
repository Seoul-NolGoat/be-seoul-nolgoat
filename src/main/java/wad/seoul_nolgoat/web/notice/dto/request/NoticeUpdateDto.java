package wad.seoul_nolgoat.web.notice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NoticeUpdateDto(
        @NotBlank @Size(max = 25, message = "제목은 25자 이내여야 합니다.")
        String title,
        @NotBlank @Size(max = 300, message = "내용은 300자 이내여야 합니다.")
        String content
) {
}