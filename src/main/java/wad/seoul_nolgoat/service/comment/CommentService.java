package wad.seoul_nolgoat.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.domain.comment.Comment;
import wad.seoul_nolgoat.domain.comment.CommentRepository;
import wad.seoul_nolgoat.domain.party.Party;
import wad.seoul_nolgoat.domain.party.PartyRepository;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.ApplicationException;
import wad.seoul_nolgoat.util.mapper.CommentMapper;
import wad.seoul_nolgoat.web.comment.dto.request.CommentSaveDto;
import wad.seoul_nolgoat.web.comment.dto.request.CommentUpdateDto;
import wad.seoul_nolgoat.web.comment.dto.response.CommentDetailsForUserDto;

import static wad.seoul_nolgoat.exception.ErrorCode.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PartyRepository partyRepository;

    @Transactional
    public Long createComment(
            String loginId,
            CommentSaveDto commentSaveDto,
            Long partyId
    ) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new ApplicationException(PARTY_NOT_FOUND));

        return commentRepository.save(CommentMapper.toEntity(commentSaveDto, user, party))
                .getId();
    }

    @Transactional
    public void update(
            String loginId,
            CommentUpdateDto commentUpdateDto,
            Long commentId
    ) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApplicationException(COMMENT_NOT_FOUND));

        if (!comment.getWriter().getLoginId().equals(loginId)) {
            throw new ApplicationException(COMMENT_UPDATE_NOT_AUTHORIZED);
        }

        comment.update(commentUpdateDto.getContent());
    }

    @Transactional
    public void delete(String loginId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApplicationException(COMMENT_NOT_FOUND));

        if (!comment.getWriter().getLoginId().equals(loginId)) {
            throw new ApplicationException(COMMENT_DELETE_NOT_AUTHORIZED);
        }

        commentRepository.delete(comment);
    }

    public Page<CommentDetailsForUserDto> findCommentsByLoginId(String loginId, Pageable pageable) {
        return commentRepository.findCommentsByLoginId(loginId, pageable);
    }
}
