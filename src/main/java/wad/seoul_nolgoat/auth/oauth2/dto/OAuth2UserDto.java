package wad.seoul_nolgoat.auth.oauth2.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuth2UserDto {

    private final String uniqueProviderId;
}
