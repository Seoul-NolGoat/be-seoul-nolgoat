package wad.seoul_nolgoat.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RedisTokenService {

    public static final String REFRESH_TOKEN_PREFIX = "RT:";
    public static final String ACCESS_TOKEN_PREFIX = "AT:";
    public static final String OAUTH2_REFRESH_TOKEN_PREFIX = "RT(oauth2):";
    public static final String OAUTH2_ACCESS_TOKEN_PREFIX = "AT(oauth2):";

    private final RedisTemplate<String, String> redisTemplate;

    public void saveToken(
            String key,
            String value,
            Date expirationDate
    ) {
        // 설정한 유효 기간까지 시간이 얼마나 남았는지 계산
        long remainingTime = expirationDate.getTime() - System.currentTimeMillis();
        redisTemplate.opsForValue()
                .set(key, value, remainingTime, TimeUnit.MILLISECONDS);
    }

    public void deleteToken(String key) {
        redisTemplate.delete(key);
    }

    // 존재하지 않으면 null을 반환
    public String getToken(String key) {
        return redisTemplate.opsForValue()
                .get(key);
    }
}
