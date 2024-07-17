package wad.seoul_nolgoat.web.review.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewDetailsForStoreDto {

    private final Long id;
    private final double grade;
    private final String content;
    private final String userNickname;
    private final String userProfileImage;
}
