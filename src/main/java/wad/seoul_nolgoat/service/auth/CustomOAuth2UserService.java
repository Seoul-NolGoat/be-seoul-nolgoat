package wad.seoul_nolgoat.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.service.auth.dto.*;

import static wad.seoul_nolgoat.exception.ErrorCode.UNSUPPORTED_PROVIDER;
import static wad.seoul_nolgoat.exception.ErrorCode.USER_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    public static final String KAKAO = "kakao";
    public static final String GOOGLE = "google";
    public static final String PROVIDER_ID_DELIMITER = "_";

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        if (registrationId.equals(KAKAO)) {
            return processOAuth2User(new KakaoResponse(oAuth2User.getAttributes()));
        }
        if (registrationId.equals(GOOGLE)) {
            return processOAuth2User(new GoogleResponse(oAuth2User.getAttributes()));
        }
        log.error("Unsupported OAuth2 provider: {}", registrationId);
        throw new ApiException(UNSUPPORTED_PROVIDER);
    }

    private OAuth2User processOAuth2User(OAuth2Response oAuth2Response) {
        String uniqueProviderId = oAuth2Response.getProvider() + PROVIDER_ID_DELIMITER + oAuth2Response.getProviderId();
        String nickname = oAuth2Response.getNickname();
        String profileImage = oAuth2Response.getProfileImage();

        if (!userRepository.existsByLoginId(uniqueProviderId)) {
            userRepository.save(
                    new User(
                            uniqueProviderId,
                            null,
                            nickname,
                            profileImage,
                            null,
                            null
                    )
            );
            OAuth2UserDto oAuth2UserDto = new OAuth2UserDto(uniqueProviderId);
            return new OAuth2UserImpl(oAuth2UserDto);
        }
        User user = userRepository.findByLoginId(uniqueProviderId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        user.update(
                null,
                nickname,
                profileImage
        );
        OAuth2UserDto oAuth2UserDto = new OAuth2UserDto(uniqueProviderId);
        return new OAuth2UserImpl(oAuth2UserDto);
    }
}
