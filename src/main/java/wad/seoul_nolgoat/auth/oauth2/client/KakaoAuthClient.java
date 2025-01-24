package wad.seoul_nolgoat.auth.oauth2.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoAuthClient", url = "https://kauth.kakao.com")
public interface KakaoAuthClient {

    @PostMapping("/oauth/token")
    TokenResponse reissueToken(
            @RequestParam("grant_type") String grantType, // "refresh_token"으로 고정
            @RequestParam("client_id") String clientId,
            @RequestParam("refresh_token") String refreshToken,
            @RequestParam("client_secret") String clientSecret
    );
}
