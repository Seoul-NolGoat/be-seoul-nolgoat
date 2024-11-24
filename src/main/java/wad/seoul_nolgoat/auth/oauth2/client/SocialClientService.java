package wad.seoul_nolgoat.auth.oauth2.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wad.seoul_nolgoat.auth.service.TokenService;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class SocialClientService {

    private static final String KAKAO_GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoApiClient kakaoApiClient;
    private final TokenService tokenService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    public void reissueKakaoToken(String loginId, String refreshToken) {
        KakaoTokenResponse response = kakaoAuthClient.reissueToken(
                KAKAO_GRANT_TYPE_REFRESH_TOKEN,
                clientId,
                refreshToken,
                clientSecret
        );
        String key = TokenService.OAUTH2_ACCESS_TOKEN_PREFIX + loginId;
        tokenService.saveToken(
                key,
                response.getAccess_token(),
                new Date(System.currentTimeMillis() + (response.getExpires_in() * 1000L))
        );
    }

    public void unlinkKakao(String accessToken) {
        UnlinkResponse response = kakaoApiClient.unlink(accessToken);
        log.info("Kakao Unlink 회원번호 : {}", response.getId());
    }
}
