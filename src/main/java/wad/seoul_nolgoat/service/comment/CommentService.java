package wad.seoul_nolgoat.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wad.seoul_nolgoat.domain.comment.CommentRepository;
import wad.seoul_nolgoat.domain.party.Party;
import wad.seoul_nolgoat.domain.party.PartyRepository;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.ApplicationException;
import wad.seoul_nolgoat.util.mapper.CommentMapper;
import wad.seoul_nolgoat.web.comment.dto.CommentSaveDto;

import static wad.seoul_nolgoat.exception.ErrorCode.PARTY_NOT_FOUND;
import static wad.seoul_nolgoat.exception.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PartyRepository partyRepository;

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
}
