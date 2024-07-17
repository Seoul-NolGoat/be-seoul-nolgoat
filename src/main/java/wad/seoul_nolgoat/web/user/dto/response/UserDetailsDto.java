package wad.seoul_nolgoat.web.user.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class UserDetailsDto {

    private final String loginId;
    private final String nickname;
    private final String profileImage;
    private final String gender;
    private final LocalDate birthDate;
}
