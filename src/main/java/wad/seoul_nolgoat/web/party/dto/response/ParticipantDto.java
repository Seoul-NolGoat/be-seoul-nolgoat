package wad.seoul_nolgoat.web.party.dto.response;

/**
 * @param participantLoginId 참여자 여부 확인을 위한 필드
 */
public record ParticipantDto(
        Long participantUserId,
        String participantNickname,
        String participantProfileImage,
        String participantLoginId
) {
}