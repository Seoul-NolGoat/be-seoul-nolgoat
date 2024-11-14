package wad.seoul_nolgoat.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class TokenService {

    public static final String REFRESH_TOKEN_PREFIX = "RT:";
    public static final String ACCESS_TOKEN_PREFIX = "AT:";

    private final RedisTemplate<String, String> redisTemplate;

    public void saveToken(
            String key,
            String value,
            Date expirationDate
    ) {
        long remainingTime = expirationDate.getTime() - System.currentTimeMillis();
        redisTemplate.opsForValue()
                .set(key, value, remainingTime);
    }

    public void deleteToken(String key) {
        redisTemplate.delete(key);
    }

    public String getToken(String key) {
        return redisTemplate.opsForValue()
                .get(key);
    }
}
