package wad.seoul_nolgoat.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.domain.refresh.RefreshToken;
import wad.seoul_nolgoat.domain.refresh.RefreshTokenRepository;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.util.mapper.UserMapper;
import wad.seoul_nolgoat.web.auth.dto.response.UserProfileDto;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static wad.seoul_nolgoat.exception.ErrorCode.USER_NOT_FOUND;

@Slf4j
@Service
public class JwtService {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 30 * 60 * 1000L; // 30 minutes
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000L; // 7 days
    private static final String CLAIM_TYPE_ACCESS = "access";
    private static final String CLAIM_TYPE_REFRESH = "refresh";
    private static final String CLAIM_TYPE = "type";
    private static final String BEARER_PREFIX = "Bearer ";

    // 로그 메시지
    private static final String INVALID_AUTHORIZATION_HEADER_MESSAGE = "유효하지 않은 Authorization 헤더입니다.";
    private static final String INVALID_ISSUER_ACCESS_TOKEN_MESSAGE = "{}는 유효하지 않은 발행자입니다.";
    private static final String INVALID_ACCESS_TOKEN_TYPE_MESSAGE = "토큰 타입이 일치하지 않습니다.";
    private static final String REFRESH_TOKEN_NULL_MESSAGE = "Refresh 토큰이 null입니다.";
    private static final String NON_EXISTENT_REFRESH_TOKEN_MESSAGE = "저장소에 존재하지 않는 Refresh 토큰입니다.";

    private final SecretKey secretKey;
    private final String domain;
    private final UserRepository userRepository;
    private final RefreshTokenRepository tokenRepository;

    public JwtService(
            @Value("${spring.jwt.secret}") String secret,
            @Value("${spring.jwt.domain}") String domain,
            UserRepository userRepository,
            RefreshTokenRepository tokenRepository
    ) {
        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
        this.domain = domain;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    // 테스트용 토큰 발급
    public String createTestToken(String loginId, String type, Long expirationTime) {
        return Jwts.builder()
                .claim(CLAIM_TYPE, type)
                .subject(loginId)
                .issuer(domain)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    // 토큰 생성 모음
    public String createAccessToken(String loginId) {
        return Jwts.builder()
                .claim(CLAIM_TYPE, CLAIM_TYPE_ACCESS)
                .subject(loginId)
                .issuer(domain)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(String loginId) {
        return Jwts.builder()
                .claim(CLAIM_TYPE, CLAIM_TYPE_REFRESH)
                .subject(loginId)
                .issuer(domain)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String reissueAccessToken(String refreshToken) {
        return Jwts.builder()
                .claim(CLAIM_TYPE, CLAIM_TYPE_ACCESS)
                .subject(getLoginId(refreshToken))
                .issuer(domain)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String reissueRefreshToken(String refreshToken) {
        return Jwts.builder()
                .claim(CLAIM_TYPE, CLAIM_TYPE_REFRESH)
                .subject(getLoginId(refreshToken))
                .issuer(domain)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    // 토큰 검증 모음
    public boolean isValidAuthorization(String authorization) {
        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            log.info(INVALID_AUTHORIZATION_HEADER_MESSAGE);

            return false;
        }

        return true;
    }

    public boolean isValidAccessToken(String accessToken) {
        Claims payload = getPayload(accessToken);
        String issuer = payload.getIssuer();
        String type = payload.get(CLAIM_TYPE, String.class);
        if (!issuer.equals(domain)) {
            log.info(INVALID_ISSUER_ACCESS_TOKEN_MESSAGE, issuer);

            return false;
        }
        if (!type.equals(CLAIM_TYPE_ACCESS)) {
            log.info(INVALID_ACCESS_TOKEN_TYPE_MESSAGE);

            return false;
        }

        return true;
    }

    public boolean isValidRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            log.info(REFRESH_TOKEN_NULL_MESSAGE);

            return false;
        }

        Claims payload = getPayload(refreshToken);
        String issuer = payload.getIssuer();
        if (!issuer.equals(domain)) {
            log.info(INVALID_ISSUER_ACCESS_TOKEN_MESSAGE, issuer);

            return false;
        }
        if (!payload.get(CLAIM_TYPE, String.class).equals(CLAIM_TYPE_REFRESH)) {
            log.info(INVALID_ACCESS_TOKEN_TYPE_MESSAGE);

            return false;
        }

        return true;
    }

    private Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 클레임 조회 모음
    public String getType(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(CLAIM_TYPE, String.class);
    }

    public String getLoginId(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // 토큰 저장소 관련 로직
    public void saveRefreshToken(String refreshToken) {
        Date date = new Date(System.currentTimeMillis() + JwtService.REFRESH_TOKEN_EXPIRATION_TIME);
        tokenRepository.save(
                new RefreshToken(
                        refreshToken,
                        getLoginId(refreshToken),
                        date.toString()
                )
        );
    }

    public boolean isExistRefreshToken(String refreshToken) {
        boolean isExistRefresh = tokenRepository.existsByRefreshToken(refreshToken);
        if (!isExistRefresh) {
            log.info(NON_EXISTENT_REFRESH_TOKEN_MESSAGE);
            return false;
        }

        return true;
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        tokenRepository.deleteByRefreshToken(refreshToken);
    }

    // 토큰 정보를 이용해 유저 정보 조회
    public UserProfileDto findLoginUserByAuthorization(String authorization) {
        String token = authorization.split(" ")[1];
        User user = userRepository.findByLoginId(getLoginId(token))
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        return UserMapper.toUserProfileDto(user);
    }
}
