package wad.seoul_nolgoat.web.notice.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wad.seoul_nolgoat.util.DateTimeUtil;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeListDto {

    private Long id;
    private String title;
    private int views;
    private Long userId;
    private String userNickname;
    private String userProfileImage;
    private String createDate;

    public NoticeListDto(
            Long id,
            String title,
            int views,
            Long userId,
            String userNickname,
            String userProfileImage,
            LocalDateTime createDate
    ) {
        this.id = id;
        this.title = title;
        this.views = views;
        this.userId = userId;
        this.userNickname = userNickname;
        this.userProfileImage = userProfileImage;
        this.createDate = DateTimeUtil.formatDate(createDate);
    }
}

