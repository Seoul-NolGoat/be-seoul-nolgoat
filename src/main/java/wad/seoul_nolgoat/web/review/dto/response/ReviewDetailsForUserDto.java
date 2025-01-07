package wad.seoul_nolgoat.web.review.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewDetailsForUserDto {

    private final Long id;
    private final int grade;
    private final String content;
    private final String imageUrl;
    private final Long storeId;
    private final String storeName;
}
