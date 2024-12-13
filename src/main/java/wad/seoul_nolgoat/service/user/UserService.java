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
import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.util.mapper.UserMapper;
import wad.seoul_nolgoat.web.user.dto.request.UserSaveDto;
import wad.seoul_nolgoat.web.user.dto.request.UserUpdateDto;
import wad.seoul_nolgoat.web.user.dto.response.UserDetailsDto;
import wad.seoul_nolgoat.web.user.dto.response.UserProfileDto;

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

    public UserProfileDto getLoginUserDetails(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        return UserMapper.toUserProfileDto(user);
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

    @Transactional
    public void deleteUserByLoginId(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        user.delete();
    }

    // OAuth2 유저 정보 동기화
    @Transactional
    public OAuth2User syncOAuth2User(String uniqueProviderId, OAuth2Response oAuth2Response) {
        String nickname = oAuth2Response.getNickname();
        String profileImage = oAuth2Response.getProfileImage();

        User user = userRepository.findByLoginId(uniqueProviderId)
                .orElse(null);

        if (user == null) {
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

        // 탈퇴 유저라면 계정을 다시 활성화
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
}
