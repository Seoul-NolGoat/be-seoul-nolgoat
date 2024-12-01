package wad.seoul_nolgoat.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.auth.oauth2.dto.OAuth2Response;
import wad.seoul_nolgoat.auth.oauth2.dto.OAuth2UserDto;
import wad.seoul_nolgoat.auth.oauth2.dto.OAuth2UserImpl;
import wad.seoul_nolgoat.auth.web.dto.response.UserProfileDto;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.util.mapper.UserMapper;
import wad.seoul_nolgoat.web.user.dto.request.UserSaveDto;
import wad.seoul_nolgoat.web.user.dto.request.UserUpdateDto;
import wad.seoul_nolgoat.web.user.dto.response.UserDetailsDto;

import static wad.seoul_nolgoat.auth.oauth2.security.CustomOAuth2UserService.PROVIDER_ID_DELIMITER;
import static wad.seoul_nolgoat.exception.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long save(UserSaveDto userSaveDto) {
        return userRepository.save(UserMapper.toEntity(userSaveDto)).getId();
    }

    public UserDetailsDto findByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        return UserMapper.toUserDetailsDto(user);
    }

    public User findByLoginId(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        return user;
    }

    public UserProfileDto getLoginUserDetails(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        return UserMapper.toUserProfileDto(user);
    }

    @Transactional
    public OAuth2User processOAuth2User(OAuth2Response oAuth2Response) {
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
        if (user.isDeleted()) {
            user.reactivate();
        }
        user.update(
                null,
                nickname,
                profileImage
        );

        OAuth2UserDto oAuth2UserDto = new OAuth2UserDto(uniqueProviderId);

        return new OAuth2UserImpl(oAuth2UserDto);
    }

    @Transactional
    public void update(Long userId, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        user.update(
                userUpdateDto.getPassword(),
                userUpdateDto.getNickname(),
                userUpdateDto.getProfileImage()
        );
    }
}
