package wad.seoul_nolgoat.web.review.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewSaveDto {

    private final double grade;
    private final String content;
}
