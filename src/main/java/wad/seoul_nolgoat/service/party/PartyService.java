package wad.seoul_nolgoat.service.party;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wad.seoul_nolgoat.domain.party.PartyRepository;
import wad.seoul_nolgoat.domain.party.PartyUserRepository;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.service.s3.S3Service;
import wad.seoul_nolgoat.util.mapper.PartyMapper;
import wad.seoul_nolgoat.web.party.request.PartySaveDto;

import java.util.Optional;

import static wad.seoul_nolgoat.exception.ErrorCode.USER_NOT_FOUND;

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
            Optional<MultipartFile> optionalMultipartFile
    ) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        Optional<String> optionalImageUrl = Optional.of(s3Service.saveFile(optionalMultipartFile.get()));

        return partyRepository.save(
                PartyMapper.toEntity(
                        partySaveDto,
                        user,
                        optionalImageUrl
                )
        ).getId();
    }

    // 파티 삭제
    public void deleteParty() {

    }

    // 파티 수정
    public void updateParty() {

    }

    // 파티 참여
    // 파티 마감
    // 참가자 밴
}
