package wad.seoul_nolgoat.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.util.mapper.UserMapper;
import wad.seoul_nolgoat.web.user.dto.request.UserSaveDto;
import wad.seoul_nolgoat.web.user.dto.request.UserUpdateDto;
import wad.seoul_nolgoat.web.user.dto.response.UserDetailsDto;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public Long save(UserSaveDto userSaveDto) {
        return userRepository.save(UserMapper.toEntity(userSaveDto)).getId();
    }

    public UserDetailsDto findByUserId(Long userId) {
        User user = userRepository.findById(userId).get();

        return UserMapper.toUserDetailsDto(user);
    }

    @Transactional
    public void update(Long userId, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userId).get();
        user.update(
                userUpdateDto.getPassword(),
                userUpdateDto.getNickname(),
                userUpdateDto.getProfileImage()
        );
    }

    @Transactional
    public void deleteById(Long userId) {
        User user = userRepository.findById(userId).get();
        user.delete();
    }
}
