package wad.seoul_nolgoat.web.inquiry.dto.response;

import java.time.LocalDateTime;

public record InquiryDetailsDto(
        Long inquiryId,
        String title,
        String content,
        Boolean isPublic,
        Long userId,
        String userNickname,
        String userProfileImage,
        LocalDateTime createDate
) {
}