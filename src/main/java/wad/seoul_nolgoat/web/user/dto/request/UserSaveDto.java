package wad.seoul_nolgoat.web.user.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class UserSaveDto {

    private final String loginId;
    private final String password;
    private final String nickname;
    private final String profileImage;
    private final String gender;
    private final LocalDate birthDate;
}
