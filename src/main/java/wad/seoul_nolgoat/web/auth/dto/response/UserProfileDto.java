package wad.seoul_nolgoat.web.auth.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserProfileDto {

    private final String username;
    private final String nickname;
    private final String profileImage;
}
