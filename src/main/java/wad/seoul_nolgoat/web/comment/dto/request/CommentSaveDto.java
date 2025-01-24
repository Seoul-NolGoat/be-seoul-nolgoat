package wad.seoul_nolgoat.web.comment.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentSaveDto {

    @NotBlank
    @Size(max = 100, message = "댓글은 100자 이내여야 합니다.")
    private final String content;

    @JsonCreator
    public CommentSaveDto(String content) {
        this.content = content;
    }
}
