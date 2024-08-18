package wad.seoul_nolgoat.service.auth.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import wad.seoul_nolgoat.web.auth.dto.request.CustomUser;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class OAuth2UserImpl implements OAuth2User, CustomUser {

    private final OAuth2UserDto oAuth2UserDto;

    // Attributes의 형태가 Response마다 다르기 때문에 사용하지 않음
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getName() {
        return oAuth2UserDto.getUniqueProviderId();
    }

    @Override
    public String getLoginId() {
        return oAuth2UserDto.getUniqueProviderId();
    }

    @Override
    public String getNickname() {
        return oAuth2UserDto.getNickname();
    }

    @Override
    public String getProfileImage() {
        return oAuth2UserDto.getProfileImage();
    }
}
