package wad.seoul_nolgoat.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.domain.party.PartyUserRepository;
import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.service.party.PartyService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wad.seoul_nolgoat.exception.ErrorCode.PARTY_CAPACITY_EXCEEDED;
import static wad.seoul_nolgoat.exception.ErrorCode.PARTY_CREATOR_CANNOT_JOIN;

@Transactional
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

    @DisplayName("본인이 생성한 파티에 참여 신청을 하면 예외가 발생합니다.")
    @Test
    void apply_join_request_when_creator_of_party_then_throw_exception() {
        // given
        String loginId = "user1";
        Long partyId = 1L;

        // when // then
        assertThatThrownBy(() -> partyService.joinParty(loginId, partyId))
                .isInstanceOf(ApiException.class)
                .hasMessage(PARTY_CREATOR_CANNOT_JOIN.getMessage());
    }

    @DisplayName("참여 가능 인원이 모두 채워진 파티에 참여 신청을 하면 예외가 발생합니다.")
    @Test
    void apply_join_request_when_party_is_full_then_throw_exception() {
        // given
        String loginIdA = "user1";
        String loginIdB = "user2";
        String loginIdD = "user4";
        String loginIdE = "user5";
        Long partyId = 3L;

        partyService.joinParty(loginIdA, partyId);
        partyService.joinParty(loginIdB, partyId);
        partyService.joinParty(loginIdD, partyId);

        // when // then
        assertThatThrownBy(() -> partyService.joinParty(loginIdE, partyId))
                .isInstanceOf(ApiException.class)
                .hasMessage(PARTY_CAPACITY_EXCEEDED.getMessage());
    }
}
