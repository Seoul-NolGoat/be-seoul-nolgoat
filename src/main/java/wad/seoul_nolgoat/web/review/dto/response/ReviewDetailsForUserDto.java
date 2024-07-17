package wad.seoul_nolgoat.web.review.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewDetailsForUserDto {

    private final double grade;
    private final String content;
    private final Long storeId;
    private final String storeName;
}
