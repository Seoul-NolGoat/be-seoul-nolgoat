package wad.seoul_nolgoat.web.notice.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NoticeSaveDto {

    private final String title;
    private final String content;
}
