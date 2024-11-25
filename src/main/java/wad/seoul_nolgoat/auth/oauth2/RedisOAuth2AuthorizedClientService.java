package wad.seoul_nolgoat.auth.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Service;
import wad.seoul_nolgoat.auth.service.RedisTokenService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class RedisOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {

    private final RedisTokenService redisTokenService;

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName) {
        return null;
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        String loginId = principal.getName();
        OAuth2RefreshToken refreshToken = authorizedClient.getRefreshToken();
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

        String key = RedisTokenService.OAUTH2_REFRESH_TOKEN_KEY_PREFIX + loginId;
        redisTokenService.saveToken(
                key,
                refreshToken.getTokenValue(),
                Date.from(
                        LocalDateTime.now()
                                .plusWeeks(2)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                )
        );

        key = RedisTokenService.OAUTH2_ACCESS_TOKEN_KEY_PREFIX + loginId;
        redisTokenService.saveToken(
                key,
                accessToken.getTokenValue(),
                Date.from(accessToken.getExpiresAt())
        );
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {

    }
}
