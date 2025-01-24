package wad.seoul_nolgoat.auth.oauth2.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoApiClient", url = "https://kapi.kakao.com")
public interface KakaoApiClient {

    @PostMapping("/v1/user/unlink")
    UnlinkResponse unlink(@RequestHeader("Authorization") String accessToken);
}
