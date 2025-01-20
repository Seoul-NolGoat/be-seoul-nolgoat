package wad.seoul_nolgoat.domain.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wad.seoul_nolgoat.web.comment.dto.response.WrittenCommentListDto;

public interface CommentRepositoryCustom {

    Page<WrittenCommentListDto> findCommentsByLoginId(String loginId, Pageable pageable);
}
