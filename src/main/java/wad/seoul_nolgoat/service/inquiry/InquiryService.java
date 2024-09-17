package wad.seoul_nolgoat.service.inquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.domain.inquiry.Inquiry;
import wad.seoul_nolgoat.domain.inquiry.InquiryRepository;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.notfound.InquiryNotFoundException;
import wad.seoul_nolgoat.exception.notfound.UserNotFoundException;
import wad.seoul_nolgoat.util.mapper.InquiryMapper;
import wad.seoul_nolgoat.web.inquiry.dto.request.InquirySaveDto;
import wad.seoul_nolgoat.web.inquiry.dto.request.InquiryUpdateDto;
import wad.seoul_nolgoat.web.inquiry.dto.response.InquiryDetailsDto;

@RequiredArgsConstructor
@Service
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    public Long save(Long userId, InquirySaveDto inquirySaveDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return inquiryRepository.save(InquiryMapper.toEntity(user, inquirySaveDto)).getId();
    }

    public InquiryDetailsDto findByInquiryId(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(InquiryNotFoundException::new);

        return InquiryMapper.toInquiryDetailsDto(inquiry);
    }

    @Transactional
    public void update(Long inquiryId, InquiryUpdateDto inquiryUpdateDto) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(InquiryNotFoundException::new);

        inquiry.update(
                inquiryUpdateDto.getTitle(),
                inquiryUpdateDto.getContent(),
                inquiryUpdateDto.getIsPublic()
        );
    }

    public void deleteById(Long inquiryId) {
        if (!inquiryRepository.existsById(inquiryId)) {
            throw new InquiryNotFoundException();
        }

        inquiryRepository.deleteById(inquiryId);
    }
}
