package wad.seoul_nolgoat.service.inquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import static wad.seoul_nolgoat.exception.ErrorCode.INQUIRY_NOT_FOUND;
import static wad.seoul_nolgoat.exception.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(String loginId, InquirySaveDto inquirySaveDto) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        return inquiryRepository.save(InquiryMapper.toEntity(user, inquirySaveDto)).getId();
    }

    public InquiryDetailsDto findByInquiryId(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ApiException(INQUIRY_NOT_FOUND));

        return InquiryMapper.toInquiryDetailsDto(inquiry);
    }

    public Page<InquiryListDto> findAllInquiryWithPagination(Pageable pageable) {
        return inquiryRepository.findAllWithPagination(pageable);
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

    @Transactional
    public void deleteById(Long inquiryId) {
        if (!inquiryRepository.existsById(inquiryId)) {
            throw new ApiException(INQUIRY_NOT_FOUND);
        }

        inquiryRepository.deleteById(inquiryId);
    }
}
