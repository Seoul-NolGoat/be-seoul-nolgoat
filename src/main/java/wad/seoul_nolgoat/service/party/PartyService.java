package wad.seoul_nolgoat.service.party;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.domain.party.*;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.ApplicationException;
import wad.seoul_nolgoat.util.mapper.PartyMapper;
import wad.seoul_nolgoat.web.party.request.PartySaveDto;
import wad.seoul_nolgoat.web.party.request.PartySearchConditionDto;
import wad.seoul_nolgoat.web.party.response.HostedPartyListDto;
import wad.seoul_nolgoat.web.party.response.PartyDetailsDto;
import wad.seoul_nolgoat.web.party.response.PartyListDto;

import static wad.seoul_nolgoat.exception.ErrorCode.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PartyService {

    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final PartyUserRepository partyUserRepository;

    // 파티 생성
    @Transactional
    public Long createParty(
            String loginId,
            PartySaveDto partySaveDto
    ) {
        validateAdministrativeDistrict(partySaveDto.getAdministrativeDistrict());

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        return partyRepository.save(PartyMapper.toEntity(partySaveDto, user))
                .getId();
    }

    // 파티 참여
    @Transactional
    public void joinParty(String loginId, Long partyId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        Long userId = user.getId();

        // 이미 파티에 참여중인 유저는 중복 신청 불가능
        if (partyUserRepository.existsByPartyIdAndParticipantId(partyId, userId)) {
            throw new ApplicationException(PARTY_ALREADY_JOINED);
        }

        while (true) {
            try {
                doJoinParty(userId, partyId);

                break;
            } catch (ObjectOptimisticLockingFailureException e) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException Ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void doJoinParty(Long userId, Long partyId) {
        Party party = partyRepository.findByIdWithFetchJoin(partyId)
                .orElseThrow(() -> new ApplicationException(PARTY_NOT_FOUND));

        // 파티 삭제 여부 확인
        if (party.isDeleted()) {
            throw new ApplicationException(PARTY_ALREADY_DELETED);
        }

        // 파티 마감 여부 확인
        if (party.isClosed()) {
            throw new ApplicationException(PARTY_ALREADY_CLOSED);
        }

        // 파티 생성자는 본인의 파티에 참여 신청 불가능
        if (party.getHost().getId().equals(userId)) {
            throw new ApplicationException(PARTY_CREATOR_CANNOT_JOIN);
        }

        // 인원 초과 여부 검증 및 참여자 수 증가
        party.incrementParticipantCount();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        PartyUser partyUser = new PartyUser(party, user);
        partyUserRepository.save(partyUser);
    }

    // 파티 수정
    @Transactional
    public void updateParty() {

    }

    // 파티 마감
    @Transactional
    public void closeById(String loginId, Long partyId) {
        Party party = partyRepository.findByIdWithFetchJoin(partyId)
                .orElseThrow(() -> new ApplicationException(PARTY_NOT_FOUND));

        if (!party.getHost().getLoginId().equals(loginId)) {
            throw new ApplicationException(PARTY_CLOSE_NOT_AUTHORIZED);
        }

        if (party.isClosed()) {
            throw new ApplicationException(PARTY_ALREADY_CLOSED);
        }
        party.close();
    }

    // 파티 삭제
    @Transactional
    public void deleteById(String loginId, Long partyId) {
        Party party = partyRepository.findByIdWithFetchJoin(partyId)
                .orElseThrow(() -> new ApplicationException(PARTY_NOT_FOUND));

        if (!party.getHost().getLoginId().equals(loginId)) {
            throw new ApplicationException(PARTY_DELETE_NOT_AUTHORIZED);
        }

        if (party.isDeleted()) {
            throw new ApplicationException(PARTY_ALREADY_DELETED);
        }

        party.delete();
    }

    // 참여자 밴
    @Transactional
    public void banParticipantFromParty(
            String loginId,
            Long partyId,
            Long userId
    ) {
        Party party = partyRepository.findByIdWithFetchJoin(partyId)
                .orElseThrow(() -> new ApplicationException(PARTY_NOT_FOUND));

        // 파티 생성자인지 검증
        if (!party.getHost().getLoginId().equals(loginId)) {
            throw new ApplicationException(PARTY_KICK_NOT_AUTHORIZED);
        }

        // 밴 대상이 참여자가 맞는지 검증
        PartyUser partyUser = partyUserRepository.findByPartyIdAndParticipantId(partyId, userId)
                .orElseThrow(() -> new ApplicationException(PARTY_USER_NOT_FOUND));

        partyUserRepository.delete(partyUser);

        // 참여자 수 감소
        party.decrementParticipantCount();
    }

    // 파티 단건 조회
    @Transactional
    public PartyDetailsDto findPartyDetailsById(Long partyId) {
        if (!partyRepository.existsById(partyId)) {
            throw new ApplicationException(PARTY_NOT_FOUND);
        }

        return partyRepository.findPartyDetailsById(partyId);
    }

    // 파티 목록 조회
    public Page<PartyListDto> findPartiesWithConditionAndPagination(PartySearchConditionDto partySearchConditionDto) {
        validateAdministrativeDistrict(partySearchConditionDto.getDistrict());

        return partyRepository.findAllWithConditionAndPagination(partySearchConditionDto);
    }

    // 내가 만든 파티 목록 조회
    public Page<HostedPartyListDto> findHostedPartiesByLoginId(String loginId, Pageable pageable) {
        return partyRepository.findHostedPartiesByLoginId(loginId, pageable);
    }

    // 내가 참여한 파티 목록 조회
    public Page<PartyListDto> findJoinedPartiesByLoginId(String loginId, Pageable pageable) {
        return partyRepository.findJoinedPartiesByLoginId(loginId, pageable);
    }

    // 유효한 지역인지 검증
    private void validateAdministrativeDistrict(String administrativeDistrict) {
        try {
            AdministrativeDistrict.valueOf(administrativeDistrict);
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(INVALID_ADMINISTRATIVE_DISTRICT, e);
        }
    }
}
