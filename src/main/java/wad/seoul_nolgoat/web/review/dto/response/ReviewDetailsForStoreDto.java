package wad.seoul_nolgoat.web.review.dto.response;

import java.time.LocalDateTime;

public record ReviewDetailsForStoreDto(
        Long reviewId,
        int grade,
        String content,
        String imageUrl,
        Long userId,
        String userNickname,
        String userProfileImage,
        LocalDateTime createDate
) {
}