package wad.seoul_nolgoat.web.notice.dto.response;

import java.time.LocalDateTime;

public record NoticeDetailsForListDto(
        Long noticeId,
        String title,
        int views,
        Long userId,
        String userNickname,
        String userProfileImage,
        LocalDateTime createDate
) {
}