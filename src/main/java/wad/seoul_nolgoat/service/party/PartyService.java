package wad.seoul_nolgoat.service.party;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wad.seoul_nolgoat.domain.party.*;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.service.s3.S3Service;
import wad.seoul_nolgoat.util.mapper.PartyMapper;
import wad.seoul_nolgoat.web.party.request.PartySaveDto;
import wad.seoul_nolgoat.web.party.response.PartyDetailsDto;

import java.time.LocalDateTime;
import java.util.Optional;

import static wad.seoul_nolgoat.exception.ErrorCode.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PartyService {

    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final PartyUserRepository partyUserRepository;

    // 파티 생성
    @Transactional
    public Long createParty(
            String loginId,
            PartySaveDto partySaveDto,
            MultipartFile image
    ) {
        validateAdministrativeDistrict(partySaveDto.getAdministrativeDistrict());

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        Optional<String> imageUrl = Optional.ofNullable(image)
                .filter(file -> !file.isEmpty())
                .map(s3Service::saveFile);

        return partyRepository.save(
                PartyMapper.toEntity(
                        partySaveDto,
                        user,
                        imageUrl.orElse(null)
                )
        ).getId();
    }

    // 파티 참여
    @Transactional
    public void joinParty(
            String loginId,
            Long partyId,
            LocalDateTime currentTime
    ) {
        Party party = partyRepository.findByIdWithLock(partyId)
                .orElseThrow(() -> new ApiException(PARTY_NOT_FOUND));

        // 파티 마감 날짜 확인
        LocalDateTime deadline = party.getDeadline();
        if (deadline.isBefore(currentTime) || deadline.isEqual(currentTime)) {
            party.close();
            throw new ApiException(PARTY_ALREADY_CLOSED);
        }

        // 파티 삭제 여부 확인
        if (party.isDeleted()) {
            throw new ApiException(PARTY_ALREADY_DELETED);
        }

        // 파티 마감 여부 확인
        if (party.isClosed()) {
            throw new ApiException(PARTY_ALREADY_CLOSED);
        }

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        Long userId = user.getId();

        // 파티 생성자는 본인의 파티에 참여 신청 불가능
        if (party.getHost().getId().equals(userId)) {
            throw new ApiException(PARTY_CREATOR_CANNOT_JOIN);
        }

        // 이미 파티에 참여중인 유저는 중복 신청 불가능
        if (partyUserRepository.existsByPartyIdAndParticipantId(partyId, userId)) {
            throw new ApiException(PARTY_ALREADY_JOINED);
        }

        int maxCapacity = party.getMaxCapacity();
        int currentCount = partyUserRepository.countByPartyId(partyId);
        if (currentCount >= maxCapacity - 1) { // 전체 인원수에서 파티 생성자는 제외
            throw new ApiException(PARTY_CAPACITY_EXCEEDED);
        }

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
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new ApiException(PARTY_NOT_FOUND));

        if (!party.getHost().getLoginId().equals(loginId)) {
            throw new ApiException(PARTY_CLOSE_NOT_AUTHORIZED);
        }

        if (party.isClosed()) {
            throw new ApiException(PARTY_ALREADY_CLOSED);
        }
        party.close();
    }

    // 파티 삭제
    @Transactional
    public void deleteById(String loginId, Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new ApiException(PARTY_NOT_FOUND));

        if (!party.getHost().getLoginId().equals(loginId)) {
            throw new ApiException(PARTY_DELETE_NOT_AUTHORIZED);
        }

        if (party.isDeleted()) {
            throw new ApiException(PARTY_ALREADY_DELETED);
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
        Party party = partyRepository.findByIdWithLock(partyId)
                .orElseThrow(() -> new ApiException(PARTY_NOT_FOUND));

        // 파티 생성자인지 검증
        if (!party.getHost().getLoginId().equals(loginId)) {
            throw new ApiException(PARTY_KICK_NOT_AUTHORIZED);
        }

        // 밴 대상이 참여자가 맞는지 검증
        PartyUser partyUser = partyUserRepository.findByPartyIdAndParticipantId(partyId, userId)
                .orElseThrow(() -> new ApiException(PARTY_USER_NOT_FOUND));

        partyUserRepository.delete(partyUser);
    }

    // 파티 단건 조회
    public PartyDetailsDto findByPartyId(Long partyId, LocalDateTime currentTime) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new ApiException(PARTY_NOT_FOUND));

        if (party.isDeleted()) {
            throw new ApiException(PARTY_ALREADY_DELETED);
        }

        if (party.getDeadline().isBefore(currentTime)) {
            party.close();
        }

        int currentCount = partyUserRepository.countByPartyId(partyId);

        return PartyMapper.toPartyDetailsDto(party, currentCount);
    }

    // 파티 리스트 조회

    // 유효한 지역인지 검증
    private void validateAdministrativeDistrict(String administrativeDistrict) {
        try {
            AdministrativeDistrict.valueOf(administrativeDistrict);
        } catch (IllegalArgumentException e) {
            throw new ApiException(INVALID_ADMINISTRATIVE_DISTRICT);
        }
    }
}
