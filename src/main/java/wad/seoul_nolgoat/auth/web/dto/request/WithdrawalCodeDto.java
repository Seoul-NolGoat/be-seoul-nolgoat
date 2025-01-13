package wad.seoul_nolgoat.auth.web.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class WithdrawalCodeDto {

    private final String inputCode;
}
