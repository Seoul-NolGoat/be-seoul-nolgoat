package wad.seoul_nolgoat.web.inquiry.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InquirySaveDto {

    private final String title;
    private final String content;
    private final Boolean isPublic;
}
