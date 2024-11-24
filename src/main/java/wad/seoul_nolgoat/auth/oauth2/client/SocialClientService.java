package wad.seoul_nolgoat.auth.oauth2.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SocialClientService {

    private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoApiClient kakaoApiClient;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    public KakaoTokenResponse reissueKakaoToken(String refreshToken) {
        return kakaoAuthClient.reissueToken(
                GRANT_TYPE_REFRESH_TOKEN,
                kakaoClientId,
                refreshToken,
                kakaoClientSecret
        );
    }

    public void unlinkKakao(String accessToken) {
        UnlinkResponse response = kakaoApiClient.unlink(accessToken);
        log.info("Kakao Unlink 회원번호 : {}", response.getId());
    }
}
