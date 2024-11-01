package wad.seoul_nolgoat.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import wad.seoul_nolgoat.domain.party.PartyUserRepository;
import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.service.party.PartyService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class PartyServiceTest {

    @Autowired
    private PartyUserRepository partyUserRepository;

    @Autowired
    private PartyService partyService;

    @DisplayName("동시에 여러 파티 가입 요청이 와도 파티의 최대 인원을 초과하지 않습니다.")
    @Test
    void prevent_exceeding_max_capacity_with_concurrent_requests() throws InterruptedException {
        //given // when
        int threadCount = 30;
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Long partyId = 1L;
        String loginIdPrefix = "user";
        for (int i = 2; i <= threadCount + 1; i++) {
            String loginId = loginIdPrefix + i;
            executorService.submit(() -> {
                try {
                    partyService.joinParty(loginId, partyId);
                } catch (ApiException e) {
                    System.out.println(e.getErrorCode().getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        assertThat(partyUserRepository.countByPartyId(1L)).isEqualTo(5);
    }
}
