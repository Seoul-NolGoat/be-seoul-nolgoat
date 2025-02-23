package wad.seoul_nolgoat.web.comment.dto.response;

import java.time.LocalDateTime;

public record CommentDetailsForPartyDto(
        Long commentId,
        String content,
        LocalDateTime createdDate,
        Boolean isDeleted,
        Long writerId,
        String writerNickname,
        String writerProfileImage
) {
}
