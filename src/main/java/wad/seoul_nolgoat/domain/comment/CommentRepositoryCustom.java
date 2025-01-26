package wad.seoul_nolgoat.domain.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wad.seoul_nolgoat.web.comment.dto.response.CommentDetailsForPartyDto;
import wad.seoul_nolgoat.web.comment.dto.response.CommentDetailsForUserDto;

import java.util.List;

public interface CommentRepositoryCustom {

    Page<CommentDetailsForUserDto> findCommentsByLoginId(String loginId, Pageable pageable);

    List<CommentDetailsForPartyDto> findCommentsByPartyId(Long partyId);
}
