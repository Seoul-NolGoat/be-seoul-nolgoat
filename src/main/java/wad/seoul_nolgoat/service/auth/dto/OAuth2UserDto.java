package wad.seoul_nolgoat.service.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuth2UserDto {

    private final String uniqueProviderId;
    private final String nickname;
    private final String profileImage;
}
