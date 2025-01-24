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
    private final GoogleClient googleClient;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    public TokenResponse reissueKakaoToken(String refreshToken) {
        return kakaoAuthClient.reissueToken(
                GRANT_TYPE_REFRESH_TOKEN,
                kakaoClientId,
                refreshToken,
                kakaoClientSecret
        );
    }

    public void unlinkKakao(String accessToken) {
        UnlinkResponse response = kakaoApiClient.unlink(accessToken);
        log.info("kakao_{} unlink successfully", response);
    }

    public TokenResponse reissueGoogleToken(String refreshToken) {
        return googleClient.reissueToken(
                GRANT_TYPE_REFRESH_TOKEN,
                googleClientId,
                refreshToken,
                googleClientSecret
        );
    }

    public void unlinkGoogle(String accessToken) {
        googleClient.unlink(accessToken);
        log.info("google unlink successfully");
    }
}
