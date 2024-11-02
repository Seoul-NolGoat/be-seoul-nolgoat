package wad.seoul_nolgoat.service.party;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wad.seoul_nolgoat.domain.party.Party;
import wad.seoul_nolgoat.domain.party.PartyRepository;
import wad.seoul_nolgoat.domain.party.PartyUser;
import wad.seoul_nolgoat.domain.party.PartyUserRepository;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.service.s3.S3Service;
import wad.seoul_nolgoat.util.mapper.PartyMapper;
import wad.seoul_nolgoat.web.party.request.PartySaveDto;

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
    public void joinParty(String loginId, Long partyId) {
        Party party = partyRepository.findByIdWithLock(partyId)
                .orElseThrow(() -> new ApiException(PARTY_NOT_FOUND));

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        Long userId = user.getId();

        // 파티 생성자는 본인의 파티에 참여 신청 불가능
        if (party.getHost().getId() == userId) {
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

    // 파티 삭제
    public void deleteParty() {

    }

    // 파티 수정
    public void updateParty() {

    }


    // 파티 마감
    // 참가자 밴
}
