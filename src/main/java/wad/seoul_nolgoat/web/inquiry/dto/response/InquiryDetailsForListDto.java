package wad.seoul_nolgoat.web.inquiry.dto.response;

import java.time.LocalDateTime;

public record InquiryDetailsForListDto(
        Long inquiryId,
        String title,
        Boolean isPublic,
        Long userId,
        String userNickname,
        String userProfileImage,
        LocalDateTime createDate
) {
}