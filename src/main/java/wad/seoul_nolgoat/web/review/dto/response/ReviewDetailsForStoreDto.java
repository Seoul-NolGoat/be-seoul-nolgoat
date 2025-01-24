package wad.seoul_nolgoat.web.review.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewDetailsForStoreDto {

    private final Long reviewId;
    private final int grade;
    private final String content;
    private final String imageUrl;
    private final Long userId;
    private final String userNickname;
    private final String userProfileImage;
    private final String createDate;
}
