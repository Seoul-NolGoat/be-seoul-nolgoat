package wad.seoul_nolgoat.web.party.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ParticipantDto {

    private final Long userId;
    private final String nickname;
    private final String profileImage;
}
