package wad.seoul_nolgoat.web.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentSaveDto(
        @NotBlank @Size(max = 100, message = "댓글은 100자 이내여야 합니다.")
        String content
) {
}
