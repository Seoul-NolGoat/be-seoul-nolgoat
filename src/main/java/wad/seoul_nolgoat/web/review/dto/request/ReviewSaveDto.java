package wad.seoul_nolgoat.web.review.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewSaveDto {

    private final int grade;
    private final String content;
}
