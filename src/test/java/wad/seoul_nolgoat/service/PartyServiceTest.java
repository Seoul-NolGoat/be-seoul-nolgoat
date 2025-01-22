package wad.seoul_nolgoat.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.domain.party.AdministrativeDistrict;
import wad.seoul_nolgoat.domain.party.PartyRepository;
import wad.seoul_nolgoat.domain.party.PartyUserRepository;
import wad.seoul_nolgoat.exception.ApplicationException;
import wad.seoul_nolgoat.service.party.PartyService;
import wad.seoul_nolgoat.web.party.request.PartySaveDto;
import wad.seoul_nolgoat.web.party.response.PartyDetailsDto;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wad.seoul_nolgoat.exception.ErrorCode.*;

@Transactional
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

    @DisplayName("파티를 생성하면, 생성된 파티의 정보가 DB에 올바르게 저장됩니다.")
    @Test
    void save_party() {
        // given
        String loginId = "user1";
        String title = "돈까스 드실 분~";
        String content = "맛있는 돈까스";
        int maxCapacity = 4;
        LocalDateTime meetingDate = LocalDateTime.of(2024, 11, 11, 12, 0);
        String administrativeDistrict = "GANGNAM_GU";
        PartySaveDto partySaveDto = new PartySaveDto(title, content, maxCapacity, meetingDate, administrativeDistrict);

        // when
        Long partyId = partyService.createParty(loginId, partySaveDto);

        // then
        assertThat(partyRepository.findById(partyId).get())
                .extracting("title", "content", "maxCapacity", "meetingDate", "administrativeDistrict", "host.loginId")
                .containsExactly(title, content, maxCapacity, meetingDate, AdministrativeDistrict.GANGNAM_GU, loginId);
    }

    @DisplayName("파티 생성 시, 잘못된 행정구역을 사용하면, 예외가 발생홥니다.")
    @Test
    void throw_exception_when_creating_party_with_invalid_district() {
        // given
        String loginId = "user1";
        String title = "돈까스 드실 분~";
        String content = "맛있는 돈까스";
        int maxCapacity = 4;
        LocalDateTime meetingDate = LocalDateTime.of(2024, 11, 11, 12, 0);
        String administrativeDistrict = "GANGNAM_GO";
        PartySaveDto partySaveDto = new PartySaveDto(title, content, maxCapacity, meetingDate, administrativeDistrict);

        // when // then
        assertThatThrownBy(() -> partyService.createParty(loginId, partySaveDto))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(INVALID_ADMINISTRATIVE_DISTRICT.getMessage());
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DisplayName("동시에 여러 유저가 파티에 가입 신청을 해도, 최대 인원을 초과하지 않습니다.")
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
                    System.out.println("==========================================참여자 : " + loginId);
                    partyService.joinParty(loginId, partyId);
                } catch (ApplicationException e) {
                    System.out.println(e.getErrorCode().getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        assertThat(partyRepository.findById(partyId).get().getCurrentCount()).isEqualTo(6);
    }

    @DisplayName("이미 삭제된 파티에 참여 신청을 하면, 예외가 발생합니다.")
    @Test
    void apply_join_request_when_party_is_already_deleted_then_throw_exception() {
        // given
        String loginId = "user2";
        Long partyId = 5L;

        // when // then
        assertThatThrownBy(() -> partyService.joinParty(loginId, partyId))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(PARTY_ALREADY_DELETED.getMessage());
    }

    @DisplayName("이미 마감된 파티에 참여 신청을 하면, 예외가 발생합니다.")
    @Test
    void apply_join_request_when_party_is_already_closed_then_throw_exception() {
        // given
        String loginId = "user2";
        Long partyId = 4L;

        // when // then
        assertThatThrownBy(() -> partyService.joinParty(loginId, partyId))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(PARTY_ALREADY_CLOSED.getMessage());
    }

    @DisplayName("본인이 생성한 파티에 참여 신청을 하면, 예외가 발생합니다.")
    @Test
    void apply_join_request_when_creator_of_party_then_throw_exception() {
        // given
        String loginId = "user1";
        Long partyId = 2L;

        // when // then
        assertThatThrownBy(() -> partyService.joinParty(loginId, partyId))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(PARTY_CREATOR_CANNOT_JOIN.getMessage());
    }

    @DisplayName("이미 참여한 파티에 참여 신청을 하면, 예외가 발생합니다.")
    @Test
    void apply_join_request_when_already_joined_party_then_throw_exception() {
        // given
        String loginIdB = "user2";
        Long partyId = 2L;

        partyService.joinParty(loginIdB, partyId);

        // when // then
        assertThatThrownBy(() -> partyService.joinParty(loginIdB, partyId))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(PARTY_ALREADY_JOINED.getMessage());
    }

    @DisplayName("참여 가능 인원이 모두 채워진 파티에 참여 신청을 하면, 예외가 발생합니다.")
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
                .isInstanceOf(ApplicationException.class)
                .hasMessage(PARTY_CAPACITY_EXCEEDED.getMessage());
    }

    // 수정 테스트

    @Transactional
    @DisplayName("파티를 마감하면, isClosed가 True로 변경됩니다.")
    @Test
    void close_party() {
        // given
        String loginId = "user1";
        Long partyId = 2L;

        // when
        partyService.closeById(loginId, partyId);

        // then
        assertThat(partyRepository.findById(partyId).get().isClosed()).isTrue();
    }

    @DisplayName("파티 생성자가 아닌 유저가 파티를 마감하려 하면, 예외가 발생합니다.")
    @Test
    void throw_exception_when_trying_to_close_party_by_non_host() {
        // given
        String loginId = "user2";
        Long partyId = 3L;

        // when // then
        assertThatThrownBy(() -> partyService.closeById(loginId, partyId))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(PARTY_CLOSE_NOT_AUTHORIZED.getMessage());
    }

    @DisplayName("이미 마감된 파티를 마감하려 하면, 예외가 발생합니다.")
    @Test
    void throw_exception_when_trying_to_close_already_closed_party() {
        // given
        String loginId = "user1";
        Long partyId = 4L;

        // when // then
        assertThatThrownBy(() -> partyService.closeById(loginId, partyId))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(PARTY_ALREADY_CLOSED.getMessage());
    }

    @Transactional
    @DisplayName("파티를 삭제하면, isDeleted가 True로 변경됩니다.")
    @Test
    void delete_party() {
        // given
        String loginId = "user1";
        Long partyId = 1L;

        // when
        partyService.deleteById(loginId, partyId);

        // then
        assertThat(partyRepository.findById(partyId).get().isDeleted()).isTrue();
    }

    @DisplayName("파티 생성자가 아닌 유저가 파티를 삭제하려 하면, 예외가 발생합니다.")
    @Test
    void throw_exception_when_trying_to_delete_party_by_non_host() {
        // given
        String loginId = "user1";
        Long partyId = 5L;

        // when // then
        assertThatThrownBy(() -> partyService.deleteById(loginId, partyId))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(PARTY_DELETE_NOT_AUTHORIZED.getMessage());
    }

    @DisplayName("이미 삭제된 파티를 삭제하려 하면, 예외가 발생합니다.")
    @Test
    void throw_exception_when_trying_to_delete_already_deleted_party() {
        // given
        String loginId = "user4";
        Long partyId = 5L;

        // when // then
        assertThatThrownBy(() -> partyService.deleteById(loginId, partyId))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(PARTY_ALREADY_DELETED.getMessage());
    }

    @DisplayName("파티 생성자가 특정 참여자를 추방하면, 해당하는 PartyUser 데이터가 삭제됩니다.")
    @Test
    void remove_participant_when_host_bans_from_party() {
        // given
        String hostLoginId = "user1";
        String participantLoginId = "user2";
        Long partyId = 2L;

        partyService.joinParty(participantLoginId, partyId);

        // when
        partyService.banParticipantFromParty(hostLoginId, partyId, 2L);

        // then
        assertThat(partyUserRepository.findAll()).hasSize(0);
    }

    @DisplayName("참여자 추방 시, 파티 생성자가 아니라면, 예외가 발생합니다.")
    @Test
    void throw_exception_when_non_host_attempts_to_ban_participant() {
        // given
        String loginId = "user3";
        String participantLoginId = "user2";
        Long partyId = 2L;

        partyService.joinParty(participantLoginId, partyId);

        // when // then
        assertThatThrownBy(() -> partyService.banParticipantFromParty(loginId, partyId, 2L))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(PARTY_KICK_NOT_AUTHORIZED.getMessage());
    }

    @DisplayName("추방 대상인 유저가 파티의 참여자가 아니라면, 예외가 발생합니다.")
    @Test
    void throw_exception_when_target_user_is_not_a_party_participant() {
        // given
        String hostLoginId = "user1";
        Long partyId = 2L;

        // when // then
        assertThatThrownBy(() -> partyService.banParticipantFromParty(hostLoginId, partyId, 2L))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(PARTY_USER_NOT_FOUND.getMessage());
    }

    @DisplayName("파티 단건 조회")
    @Test
    void update_party_to_closed_when_finding_party_after_meeting_date() {
        // given
        Long partyId = 2L;

        PartyDetailsDto a = partyService.findPartyDetailsById(partyId);

        System.out.println(a.getTitle());

        // when // then
        assertThat(a)
                .extracting(
                        "id",
                        "title",
                        "content",
                        "maxCapacity",
                        "meetingDate",
                        "isClosed",
                        "currentCount"
                )
                .containsExactly(
                        2L,
                        "PartyB",
                        "Party Content B",
                        6,
                        LocalDateTime.of(2024, 12, 31, 23, 59, 59),
                        false,
                        1
                );
    }
}
