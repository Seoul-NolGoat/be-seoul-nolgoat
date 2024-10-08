package wad.seoul_nolgoat.service.inquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.domain.inquiry.Inquiry;
import wad.seoul_nolgoat.domain.inquiry.InquiryRepository;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.util.mapper.InquiryMapper;
import wad.seoul_nolgoat.web.inquiry.dto.request.InquirySaveDto;
import wad.seoul_nolgoat.web.inquiry.dto.request.InquiryUpdateDto;
import wad.seoul_nolgoat.web.inquiry.dto.response.InquiryDetailsDto;
import wad.seoul_nolgoat.web.inquiry.dto.response.InquiryListDto;

import java.util.List;
import java.util.stream.Collectors;

import static wad.seoul_nolgoat.exception.ErrorCode.INQUIRY_NOT_FOUND;
import static wad.seoul_nolgoat.exception.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    public Long save(Long userId, InquirySaveDto inquirySaveDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        return inquiryRepository.save(InquiryMapper.toEntity(user, inquirySaveDto)).getId();
    }

    public InquiryDetailsDto findByInquiryId(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ApiException(INQUIRY_NOT_FOUND));

        return InquiryMapper.toInquiryDetailsDto(inquiry);
    }

    public List<InquiryListDto> findAllInquiry() {
        List<Inquiry> inquiries = inquiryRepository.findAll();

        return inquiries.stream()
                .map(InquiryMapper::toInquiryListDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(Long inquiryId, InquiryUpdateDto inquiryUpdateDto) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ApiException(INQUIRY_NOT_FOUND));

        inquiry.update(
                inquiryUpdateDto.getTitle(),
                inquiryUpdateDto.getContent(),
                inquiryUpdateDto.getIsPublic()
        );
    }

    public void deleteById(Long inquiryId) {
        if (!inquiryRepository.existsById(inquiryId)) {
            throw new ApiException(INQUIRY_NOT_FOUND);
        }

        inquiryRepository.deleteById(inquiryId);
    }
}
