package wad.seoul_nolgoat.web.inquiry.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class InquiryDetailsDto {

    private final Long inquiryId;
    private final String title;
    private final String content;
    private final Boolean isPublic;
    private final Long userId;
    private final String userNickname;
    private final String userProfileImage;
    private final LocalDateTime createDate;
}
