package wad.seoul_nolgoat.service.party;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.domain.comment.CommentRepository;
import wad.seoul_nolgoat.domain.party.AdministrativeDistrict;
import wad.seoul_nolgoat.domain.party.Party;
import wad.seoul_nolgoat.domain.party.PartyRepository;
import wad.seoul_nolgoat.domain.partyuser.PartyUser;
import wad.seoul_nolgoat.domain.partyuser.PartyUserRepository;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.ApplicationException;
import wad.seoul_nolgoat.util.mapper.PartyMapper;
import wad.seoul_nolgoat.web.comment.dto.response.CommentDetailsForPartyDto;
import wad.seoul_nolgoat.web.party.dto.request.PartySaveDto;
import wad.seoul_nolgoat.web.party.dto.request.PartySearchConditionDto;
import wad.seoul_nolgoat.web.party.dto.request.PartyUpdateDto;
import wad.seoul_nolgoat.web.party.dto.response.ParticipantDto;
import wad.seoul_nolgoat.web.party.dto.response.PartyDetailsDto;
import wad.seoul_nolgoat.web.party.dto.response.PartyDetailsForListDto;
import wad.seoul_nolgoat.web.party.dto.response.PartyDetailsForUserDto;

import java.time.LocalDateTime;
import java.util.List;

import static wad.seoul_nolgoat.exception.ErrorCode.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PartyService {

    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final PartyUserRepository partyUserRepository;
    private final CommentRepository commentRepository;

    // 파티 생성
    @Transactional
    public Long createParty(
            String loginId,
            PartySaveDto partySaveDto
    ) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        return partyRepository.save(PartyMapper.toEntity(partySaveDto, user))
                .getId();
    }

    // 파티 참여
    @Retryable(
            value = OptimisticLockingFailureException.class,
            backoff = @Backoff(delay = 100, multiplier = 1.5)
    )
    @Transactional
    public void joinParty(String loginId, Long partyId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        // 이미 파티에 참여중인 유저는 중복 신청 불가능
        if (partyUserRepository.existsByPartyIdAndParticipantId(partyId, user.getId())) {
            throw new ApplicationException(PARTY_ALREADY_JOINED);
        }

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
        if (loginId.equals(party.getHost().getLoginId())) {
            throw new ApplicationException(PARTY_CREATOR_CANNOT_JOIN);
        }

        // partyUser 객체 저장 및 유니크 제약 조건 위반 예외 처리
        try {
            PartyUser partyUser = new PartyUser(party, user);
            partyUserRepository.save(partyUser);
        } catch (DuplicateKeyException e) {
            throw new ApplicationException(PARTY_ALREADY_JOINED);
        }

        // 인원 초과 여부 검증 및 참여자 수 증가
        party.incrementParticipantCount();
    }

    // 파티 탈퇴
    @Retryable(
            value = OptimisticLockingFailureException.class,
            backoff = @Backoff(delay = 100, multiplier = 1.5)
    )
    @Transactional
    public void leave(String loginId, Long partyId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        Party party = partyRepository.findByIdWithFetchJoin(partyId)
                .orElseThrow(() -> new ApplicationException(PARTY_NOT_FOUND));

        // 파티 호스트인지 확인
        if (loginId.equals(party.getHost().getLoginId())) {
            throw new ApplicationException(PARTY_CREATOR_CANNOT_LEAVE);
        }

        // 파티 참여자가 맞는지 확인
        PartyUser partyUser = partyUserRepository.findByPartyIdAndParticipantId(partyId, user.getId())
                .orElseThrow(() -> new ApplicationException(PARTY_USER_NOT_FOUND));

        partyUserRepository.delete(partyUser);

        // 현재 인원 수 검증 및 참여자 수 감소
        party.decrementParticipantCount();
    }

    // 파티 마감
    @Transactional
    public void close(String loginId, Long partyId) {
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

    // 파티 수정
    @Transactional
    public void update(
            PartyUpdateDto partyUpdateDto,
            String loginId,
            Long partyId
    ) {
        Party party = partyRepository.findByIdWithFetchJoin(partyId)
                .orElseThrow(() -> new ApplicationException(PARTY_NOT_FOUND));

        // 해당 파티의 호스트인지 검증
        if (!party.getHost().getLoginId().equals(loginId)) {
            throw new ApplicationException(PARTY_UPDATE_NOT_AUTHORIZED);
        }

        if (party.isClosed()) {
            throw new ApplicationException(PARTY_ALREADY_CLOSED);
        }

        if (party.isDeleted()) {
            throw new ApplicationException(PARTY_ALREADY_DELETED);
        }

        // 수정된 참여 가능 전체 인원수가 현재 인원수보다 적은지 검증
        if (partyUpdateDto.maxCapacity() < party.getCurrentCount()) {
            throw new ApplicationException(INVALID_MAX_CAPACITY);
        }

        party.update(
                partyUpdateDto.title(),
                partyUpdateDto.content(),
                partyUpdateDto.maxCapacity(),
                partyUpdateDto.meetingDate(),
                AdministrativeDistrict.fromString(partyUpdateDto.administrativeDistrict())
        );
    }

    // 파티 삭제
    @Transactional
    public void delete(String loginId, Long partyId) {
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
    public PartyDetailsDto findPartyDetailsById(String loginId, Long partyId) {
        Party party = partyRepository.findPartyByIdWithFetchJoin(partyId)
                .orElseThrow(() -> new ApplicationException(PARTY_NOT_FOUND));

        // 참여 일 검증
        LocalDateTime currentTime = LocalDateTime.now();
        if (!party.isClosed()) {
            if (party.getMeetingDate().isBefore(currentTime) || party.getMeetingDate().isEqual(currentTime)) {
                party.close();
            }
        }

        List<ParticipantDto> participants = partyUserRepository.findParticipantsByPartyId(partyId);
        List<CommentDetailsForPartyDto> comments = commentRepository.findCommentsByPartyId(partyId);

        return PartyDetailsDto.of(
                party,
                participants,
                comments,
                loginId
        );
    }

    // 파티 목록 조회
    @Transactional
    public Page<PartyDetailsForListDto> findPartiesWithConditionAndPagination(PartySearchConditionDto partySearchConditionDto) {
        Page<Party> parties = partyRepository.findAllWithConditionAndPagination(partySearchConditionDto);
        closeExpiredParties(parties);

        return parties.map(PartyDetailsForListDto::from);
    }

    // 내가 만든 파티 목록 조회
    @Transactional
    public Page<PartyDetailsForUserDto> findHostedPartiesByLoginId(String loginId, Pageable pageable) {
        Page<Party> parties = partyRepository.findHostedPartiesByLoginId(loginId, pageable);
        closeExpiredParties(parties);

        return parties.map(PartyDetailsForUserDto::from);
    }

    // 내가 참여한 파티 목록 조회
    @Transactional
    public Page<PartyDetailsForListDto> findJoinedPartiesByLoginId(String loginId, Pageable pageable) {
        Page<Party> parties = partyRepository.findJoinedPartiesByLoginId(loginId, pageable);
        closeExpiredParties(parties);

        return parties.map(PartyDetailsForListDto::from);
    }

    private void closeExpiredParties(Page<Party> parties) {
        LocalDateTime currentTime = LocalDateTime.now();

        // 현재 시간과 비교하여 마감 여부 결정
        parties.getContent().forEach(party -> {
            if (!party.isClosed()) {
                if (party.getMeetingDate().isBefore(currentTime) || party.getMeetingDate().isEqual(currentTime)) {
                    party.close();
                }
            }
        });
    }
}
