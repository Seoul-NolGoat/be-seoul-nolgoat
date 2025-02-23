package wad.seoul_nolgoat.web.notice.dto.response;

import java.time.LocalDateTime;

public record NoticeDetailsDto(
        Long noticeId,
        String title,
        String content,
        int views,
        Long userId,
        String userNickname,
        String userProfileImage,
        LocalDateTime createDate
) {
}