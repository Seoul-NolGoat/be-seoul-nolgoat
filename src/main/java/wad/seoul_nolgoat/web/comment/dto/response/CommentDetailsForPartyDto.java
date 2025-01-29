package wad.seoul_nolgoat.web.comment.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CommentDetailsForPartyDto {

    private final Long commentId;
    private final String content;
    private final LocalDateTime createdDate;
    private final Long writerId;
    private final String writerNickname;
    private final String writerProfileImage;
}
