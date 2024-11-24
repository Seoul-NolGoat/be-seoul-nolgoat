package wad.seoul_nolgoat.auth.oauth2.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoTokenResponse {

    private final String token_type;
    private final String access_token;
    private final int expires_in;
}
