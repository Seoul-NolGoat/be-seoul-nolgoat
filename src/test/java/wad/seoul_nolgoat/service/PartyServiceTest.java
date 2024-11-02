package wad.seoul_nolgoat.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.domain.party.PartyRepository;
import wad.seoul_nolgoat.domain.party.PartyUserRepository;
import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.service.party.PartyService;
import wad.seoul_nolgoat.web.party.request.PartySaveDto;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wad.seoul_nolgoat.exception.ErrorCode.*;

@ActiveProfiles("test")
@SpringBootTest
public class PartyServiceTest {

    @Autowired
    private PartyService partyService;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private PartyUserRepository partyUserRepository;

    @AfterEach
    void tearDown() {
        partyUserRepository.deleteAllInBatch();
    }

    @Transactional
    @DisplayName("파티를 생성하면 생성된 파티의 정보가 DB에 올바르게 저장됩니다.")
    @Test
    void save_party() {
        // given
        String loginId = "user1";
        String title = "돈까스 드실 분~";
        int maxCapacity = 4;
        LocalDateTime deadline = LocalDateTime.of(2024, 11, 11, 12, 0);
        PartySaveDto partySaveDto = new PartySaveDto(title, maxCapacity, deadline);

        // when
        Long partyId = partyService.createParty(loginId, partySaveDto, null);

        // then
        assertThat(partyRepository.findById(partyId).get())
                .extracting("title", "maxCapacity", "deadline")
                .containsExactly(title, maxCapacity, deadline);
    }

    @DisplayName("동시에 여러 유저가 파티에 가입 신청을 해도 최대 인원을 초과하지 않습니다.")
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

    @DisplayName("이미 마감된 파티에 참여 신청을 하면 예외가 발생합니다.")
    @Test
    void apply_join_request_when_party_is_already_closed_then_throw_exception() {
        // given
        String loginId = "user2";
        Long partyId = 4L;

        // when // then
        assertThatThrownBy(() -> partyService.joinParty(loginId, partyId))
                .isInstanceOf(ApiException.class)
                .hasMessage(PARTY_ALREADY_CLOSED.getMessage());
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

    @DisplayName("이미 참여한 파티에 참여 신청을 하면 예외가 발생합니다.")
    @Test
    void apply_join_request_when_already_joined_party_then_throw_exception() {
        // given
        String loginIdB = "user2";
        Long partyId = 1L;

        partyService.joinParty(loginIdB, partyId);

        // when // then
        assertThatThrownBy(() -> partyService.joinParty(loginIdB, partyId))
                .isInstanceOf(ApiException.class)
                .hasMessage(PARTY_ALREADY_JOINED.getMessage());
    }

    // 수정 테스트

    @Transactional
    @DisplayName("파티를 마감하면, isClosed가 True로 변경됩니다.")
    @Test
    void close_party() {
        // given
        Long partyId = 1L;

        // when
        partyService.closeById(partyId);

        // then
        assertThat(partyRepository.findById(partyId).get().isClosed()).isTrue();
    }

    @DisplayName("이미 마감된 파티를 마감하려 하면, 예외가 발생합니다.")
    @Test
    void throw_exception_when_trying_to_close_already_closed_party() {
        // given
        Long partyId = 4L;

        // when // then
        assertThatThrownBy(() -> partyService.closeById(partyId))
                .isInstanceOf(ApiException.class)
                .hasMessage(PARTY_ALREADY_CLOSED.getMessage());
    }

    // 삭제 테스트

    // 참여자 밴 테스트
}
