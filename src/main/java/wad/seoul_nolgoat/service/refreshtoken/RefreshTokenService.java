package wad.seoul_nolgoat.service.refreshtoken;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.auth.jwt.JwtService;
import wad.seoul_nolgoat.domain.refreshtoken.RefreshToken;
import wad.seoul_nolgoat.domain.refreshtoken.RefreshTokenRepository;
import wad.seoul_nolgoat.exception.ApiException;

import java.util.Date;

import static wad.seoul_nolgoat.exception.ErrorCode.REFRESH_TOKEN_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(String refreshToken, String loginId) {
        Date date = new Date(System.currentTimeMillis() + JwtService.REFRESH_TOKEN_EXPIRATION_TIME);
        refreshTokenRepository.save(
                new RefreshToken(
                        refreshToken,
                        loginId,
                        date.toString()
                )
        );
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }

    public void verifyRefreshTokenExistence(String refreshToken) {
        if (!refreshTokenRepository.existsByRefreshToken(refreshToken)) {
            throw new ApiException(REFRESH_TOKEN_NOT_FOUND);
        }
    }
}
