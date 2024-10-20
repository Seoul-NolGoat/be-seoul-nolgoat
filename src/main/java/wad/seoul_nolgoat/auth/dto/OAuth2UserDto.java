package wad.seoul_nolgoat.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuth2UserDto {

    private final String uniqueProviderId;
}
