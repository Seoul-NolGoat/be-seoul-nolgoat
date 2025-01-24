package wad.seoul_nolgoat.web.party.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ParticipantDto {

    private final Long participantUserId;
    private final String participantNickname;
    private final String participantProfileImage;

    // 참여자 여부 확인을 위한 필드
    private final String participantLoginId;
}
