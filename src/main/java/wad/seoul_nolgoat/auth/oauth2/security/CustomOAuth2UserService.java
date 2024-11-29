package wad.seoul_nolgoat.auth.oauth2.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.auth.oauth2.dto.GoogleResponse;
import wad.seoul_nolgoat.auth.oauth2.dto.KakaoResponse;
import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.service.user.UserService;

import static wad.seoul_nolgoat.exception.ErrorCode.UNSUPPORTED_PROVIDER;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    public static final String KAKAO = "kakao";
    public static final String GOOGLE = "google";
    public static final String PROVIDER_ID_DELIMITER = "_";

    private final UserService userService;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        if (registrationId.equals(KAKAO)) {
            return userService.processOAuth2User(new KakaoResponse(oAuth2User.getAttributes()));
        }
        if (registrationId.equals(GOOGLE)) {
            return userService.processOAuth2User(new GoogleResponse(oAuth2User.getAttributes()));
        }
        log.error("Unsupported OAuth2 provider: {}", registrationId);
        throw new ApiException(UNSUPPORTED_PROVIDER);
    }
}
