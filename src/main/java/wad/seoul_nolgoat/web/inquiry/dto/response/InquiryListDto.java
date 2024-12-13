package wad.seoul_nolgoat.web.inquiry.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wad.seoul_nolgoat.util.DateTimeUtil;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryListDto {

    private Long id;
    private String title;
    private Boolean isPublic;
    private Long userId;
    private String userNickname;
    private String userProfileImage;
    private String createDate;

    public InquiryListDto(Long id, String title, Boolean isPublic, Long userId, String userNickname, String userProfileImage, LocalDateTime createDate) {
        this.id = id;
        this.title = title;
        this.isPublic = isPublic;
        this.userId = userId;
        this.userNickname = userNickname;
        this.userProfileImage = userProfileImage;
        this.createDate = DateTimeUtil.formatDate(createDate);
    }
}
