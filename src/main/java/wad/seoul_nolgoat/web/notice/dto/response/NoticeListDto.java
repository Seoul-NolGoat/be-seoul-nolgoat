package wad.seoul_nolgoat.web.notice.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NoticeListDto {

    private final Long id;
    private final String title;
    private final int views;
    private final Long userId;
    private final String userNickname;
    private final String userProfileImage;
    private final String createDate;
}
