package wad.seoul_nolgoat.web.comment.dto.response;

import java.time.LocalDateTime;

public record CommentDetailsForUserDto(
        Long commentId,
        String content,
        LocalDateTime createdDate,
        Boolean isDeleted,
        Long partyId
) {
}
