package wad.seoul_nolgoat.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.domain.refresh.RefreshToken;
import wad.seoul_nolgoat.domain.refresh.RefreshTokenRepository;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.notfound.UserNotFoundException;
import wad.seoul_nolgoat.jwt.JwtUtil;
import wad.seoul_nolgoat.util.mapper.UserMapper;
import wad.seoul_nolgoat.web.auth.dto.response.UserProfileDto;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    public static final String AUTHORIZATION_HEADER_TYPE = "Authorization";
    public static final String REFRESH_HEADER_TYPE = "Refresh";

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String TOKEN_EXPIRED_MESSAGE = "Token has expired";
    private static final String INVALID_AUTHORIZATION_MESSAGE = "Authorization is null or invalid";
    private static final String NULL_REFRESH_TOKEN_MESSAGE = "Refresh token is null";
    private static final String INVALID_REFRESH_TOKEN_MESSAGE = "Refresh token is invalid";
    private static final String NON_EXISTENT_REFRESH_TOKEN_MESSAGE = "Refresh token is not exist";

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public UserProfileDto findLoginUserByAuthorization(String authorization) {
        String token = authorization.split(" ")[1];
        User user = userRepository.findByLoginId(jwtUtil.getLoginId(token))
                .orElseThrow(UserNotFoundException::new);

        return UserMapper.toUserProfileDto(user);
    }

    public boolean isInvalidAuthorization(String authorization) {
        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            log.info(INVALID_AUTHORIZATION_MESSAGE);

            return true;
        }

        return false;
    }

    public boolean isInvalidRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            log.info(NULL_REFRESH_TOKEN_MESSAGE);

            return true;
        }
        if (!jwtUtil.getType(refreshToken).equals(JwtUtil.CLAIM_TYPE_REFRESH)) {
            log.info(INVALID_REFRESH_TOKEN_MESSAGE);

            return true;
        }

        return false;
    }

    public boolean isExpiredToken(String token) {
        if (jwtUtil.isExpired(token)) {
            log.info(TOKEN_EXPIRED_MESSAGE);

            return true;
        }

        return false;
    }

    public boolean isExistRefreshToken(String refreshToken) {
        boolean isExistRefresh = refreshTokenRepository.existsByRefreshToken(refreshToken);
        if (!isExistRefresh) {
            log.info(NON_EXISTENT_REFRESH_TOKEN_MESSAGE);
        }

        return isExistRefresh;
    }

    public String createAccessToken(String loginId) {
        return jwtUtil.createJwt(
                JwtUtil.CLAIM_TYPE_ACCESS,
                loginId,
                JwtUtil.ACCESS_TOKEN_EXPIRATION_TIME
        );
    }

    public String createRefreshToken(String loginId) {
        return jwtUtil.createJwt(
                JwtUtil.CLAIM_TYPE_REFRESH,
                loginId,
                JwtUtil.REFRESH_TOKEN_EXPIRATION_TIME
        );
    }

    public String getNewAccessToken(String refreshToken) {
        return jwtUtil.createJwt(
                JwtUtil.CLAIM_TYPE_ACCESS,
                jwtUtil.getLoginId(refreshToken),
                JwtUtil.ACCESS_TOKEN_EXPIRATION_TIME
        );
    }

    public String getNewRefreshToken(String refreshToken) {
        return jwtUtil.createJwt(
                JwtUtil.CLAIM_TYPE_REFRESH,
                jwtUtil.getLoginId(refreshToken),
                JwtUtil.REFRESH_TOKEN_EXPIRATION_TIME
        );
    }

    public void saveRefreshToken(String refreshToken) {
        Date date = new Date(System.currentTimeMillis() + JwtUtil.REFRESH_TOKEN_EXPIRATION_TIME);
        refreshTokenRepository.save(
                new RefreshToken(
                        refreshToken,
                        jwtUtil.getLoginId(refreshToken),
                        date.toString()
                )
        );
    }

    public String getLoginIdFromToken(String token) {
        return jwtUtil.getLoginId(token);
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }
}
