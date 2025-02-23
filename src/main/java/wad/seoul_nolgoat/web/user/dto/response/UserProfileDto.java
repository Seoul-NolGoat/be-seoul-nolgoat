package wad.seoul_nolgoat.web.user.dto.response;

public record UserProfileDto(
        Long userId,
        String loginId,
        String nickname,
        String profileImage
) {
}