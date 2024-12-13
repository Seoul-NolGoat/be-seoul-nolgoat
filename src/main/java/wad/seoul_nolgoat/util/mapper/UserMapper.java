package wad.seoul_nolgoat.util.mapper;

import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.web.user.dto.request.UserSaveDto;
import wad.seoul_nolgoat.web.user.dto.response.UserProfileDto;

public class UserMapper {

    public static User toEntity(UserSaveDto userSaveDto) {
        return new User(
                userSaveDto.getLoginId(),
                userSaveDto.getPassword(),
                userSaveDto.getNickname(),
                userSaveDto.getProfileImage(),
                userSaveDto.getGender(),
                userSaveDto.getBirthDate()
        );
    }

    public static UserProfileDto toUserProfileDto(User user) {
        return new UserProfileDto(
                user.getId(),
                user.getLoginId(),
                user.getNickname(),
                user.getProfileImage()
        );
    }
}
