package wad.seoul_nolgoat.util.mapper;

import wad.seoul_nolgoat.domain.notice.Notice;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.web.notice.dto.request.NoticeSaveDto;
import wad.seoul_nolgoat.web.notice.dto.response.NoticeDetailsDto;

public class NoticeMapper {

    public static Notice toEntity(User user, NoticeSaveDto noticeSaveDto) {
        return new Notice(
                noticeSaveDto.getTitle(),
                noticeSaveDto.getContent(),
                user
        );
    }

    public static NoticeDetailsDto toNoticeDetailsDto(Notice notice) {
        return new NoticeDetailsDto(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getViews(),
                notice.getUser().getId(),
                notice.getUser().getNickname(),
                notice.getUser().getProfileImage(),
                notice.getCreatedDate()
        );
    }
}