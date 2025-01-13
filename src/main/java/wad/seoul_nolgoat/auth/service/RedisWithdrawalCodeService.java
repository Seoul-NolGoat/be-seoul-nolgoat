package wad.seoul_nolgoat.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RedisWithdrawalCodeService {

    private static final String WITHDRAWAL_CODE_PREFIX = "WITHDRAWAL:CODE:";
    private static final long WITHDRAWAL_CODE_EXPIRATION = 5 * 60 * 1000L; // 5분

    private final RedisTemplate<String, String> redisTemplate;

    public void saveCode(String loginId, String verificationCode) {
        String key = WITHDRAWAL_CODE_PREFIX + loginId;
        redisTemplate.opsForValue()
                .set(key, verificationCode, WITHDRAWAL_CODE_EXPIRATION, TimeUnit.MILLISECONDS);
    }

    public String getCode(String loginId) {
        String key = WITHDRAWAL_CODE_PREFIX + loginId;
        return redisTemplate.opsForValue()
                .get(key);
    }
}
