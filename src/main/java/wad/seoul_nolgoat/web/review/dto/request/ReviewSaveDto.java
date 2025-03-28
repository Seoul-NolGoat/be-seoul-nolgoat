package wad.seoul_nolgoat.web.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReviewSaveDto(
        @Min(value = 1, message = "평점은 1 이상이어야 합니다.") @Max(value = 5, message = "평점은 5 이하여야 합니다.")
        int grade,
        @NotBlank @Size(max = 150)
        String content
) {
}