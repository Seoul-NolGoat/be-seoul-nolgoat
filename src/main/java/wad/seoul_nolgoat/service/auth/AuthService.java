package wad.seoul_nolgoat.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.jwt.JwtUtil;
import wad.seoul_nolgoat.util.mapper.UserMapper;
import wad.seoul_nolgoat.web.auth.dto.response.UserProfileDto;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserProfileDto findLoginUserByAuthorization(String authorization) {
        String token = authorization.split(" ")[1];
        User user = userRepository.findByLoginId(jwtUtil.getLoginId(token)).get();

        return UserMapper.toUserProfileDto(user);
    }
}
