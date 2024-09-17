package wad.seoul_nolgoat.web.inquiry.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InquiryUpdateDto {

    private final String title;
    private final String content;
    private final Boolean isPublic;
}
