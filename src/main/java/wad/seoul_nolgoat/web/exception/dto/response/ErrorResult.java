package wad.seoul_nolgoat.web.exception.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResult {

    private final String message;
}
