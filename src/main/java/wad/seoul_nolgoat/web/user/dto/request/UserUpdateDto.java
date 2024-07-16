package wad.seoul_nolgoat.web.user.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserUpdateDto {

    private final String password;
    private final String nickname;
    private final String profileImage;
}
