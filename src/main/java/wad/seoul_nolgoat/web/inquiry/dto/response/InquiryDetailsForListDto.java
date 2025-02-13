package wad.seoul_nolgoat.web.inquiry.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryDetailsForListDto {

    private Long inquiryId;
    private String title;
    private Boolean isPublic;
    private Long userId;
    private String userNickname;
    private String userProfileImage;
    private LocalDateTime createDate;

    public InquiryDetailsForListDto(
            Long inquiryId,
            String title,
            Boolean isPublic,
            Long userId,
            String userNickname,
            String userProfileImage,
            LocalDateTime createDate
    ) {
        this.inquiryId = inquiryId;
        this.title = title;
        this.isPublic = isPublic;
        this.userId = userId;
        this.userNickname = userNickname;
        this.userProfileImage = userProfileImage;
        this.createDate = createDate;
    }
}
