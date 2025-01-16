package wad.seoul_nolgoat.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.auth.oauth2.dto.OAuth2Response;
import wad.seoul_nolgoat.auth.oauth2.dto.OAuth2UserDto;
import wad.seoul_nolgoat.auth.oauth2.dto.OAuth2UserImpl;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.ApplicationException;
import wad.seoul_nolgoat.util.mapper.UserMapper;
import wad.seoul_nolgoat.web.user.dto.response.UserProfileDto;

import static wad.seoul_nolgoat.exception.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserProfileDto getLoginUserDetails(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        return UserMapper.toUserProfileDto(user);
    }

    @Transactional
    public void deleteUserByLoginId(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        user.delete();
    }

    // OAuth2 유저 정보 동기화
    @Transactional
    public OAuth2User syncOAuth2User(String uniqueProviderId, OAuth2Response oAuth2Response) {
        String nickname = oAuth2Response.getNickname();
        String profileImage = oAuth2Response.getProfileImage();
        String email = oAuth2Response.getEmail();

        User user = userRepository.findByLoginId(uniqueProviderId)
                .orElse(null);

        if (user == null) {
            userRepository.save(
                    new User(
                            uniqueProviderId,
                            null,
                            nickname,
                            profileImage,
                            email
                    )
            );
            OAuth2UserDto oAuth2UserDto = new OAuth2UserDto(uniqueProviderId);
            return new OAuth2UserImpl(oAuth2UserDto);
        }

        // 탈퇴 유저라면 계정을 다시 활성화
        if (user.isDeleted()) {
            user.reactivate();
        }
        user.update(
                nickname,
                profileImage,
                email
        );

        OAuth2UserDto oAuth2UserDto = new OAuth2UserDto(uniqueProviderId);

        return new OAuth2UserImpl(oAuth2UserDto);
    }
}
