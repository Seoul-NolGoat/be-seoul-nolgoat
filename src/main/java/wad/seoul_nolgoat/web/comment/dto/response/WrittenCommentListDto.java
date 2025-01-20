package wad.seoul_nolgoat.web.comment.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class WrittenCommentListDto {

    private final Long id;
    private final String content;
    private final LocalDateTime createdDate;
    private final Long partyId;
}
