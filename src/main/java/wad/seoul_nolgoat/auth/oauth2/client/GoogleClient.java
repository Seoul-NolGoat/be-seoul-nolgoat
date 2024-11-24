package wad.seoul_nolgoat.auth.oauth2.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleClient", url = "https://oauth2.googleapis.com")
public interface GoogleClient {

    @PostMapping("/token")
    TokenResponse reissueToken(
            @RequestParam("grant_type") String grantType, // "refresh_token"으로 고정
            @RequestParam("client_id") String clientId,
            @RequestParam("refresh_token") String refreshToken,
            @RequestParam("client_secret") String clientSecret
    );

    // Refresh, Access 토큰 둘 다 사용 가능
    @PostMapping("/revoke")
    void unlink(@RequestParam("token") String accessToken);
}
