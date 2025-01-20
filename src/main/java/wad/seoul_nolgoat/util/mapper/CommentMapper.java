package wad.seoul_nolgoat.util.mapper;

import wad.seoul_nolgoat.domain.comment.Comment;
import wad.seoul_nolgoat.domain.party.Party;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.web.comment.dto.CommentSaveDto;

public class CommentMapper {

    public static Comment toEntity(
            CommentSaveDto commentSaveDto,
            User user,
            Party party
    ) {
        return new Comment(
                user,
                party,
                commentSaveDto.getContent()
        );
    }
}
