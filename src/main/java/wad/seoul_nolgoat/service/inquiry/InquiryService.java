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
import wad.seoul_nolgoat.exception.ApplicationException;
import wad.seoul_nolgoat.util.mapper.InquiryMapper;
import wad.seoul_nolgoat.web.inquiry.dto.request.InquirySaveDto;
import wad.seoul_nolgoat.web.inquiry.dto.request.InquiryUpdateDto;
import wad.seoul_nolgoat.web.inquiry.dto.response.InquiryDetailsDto;
import wad.seoul_nolgoat.web.inquiry.dto.response.InquiryDetailsForListDto;

import static wad.seoul_nolgoat.exception.ErrorCode.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(String loginId, InquirySaveDto inquirySaveDto) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        return inquiryRepository.save(InquiryMapper.toEntity(user, inquirySaveDto)).getId();
    }

    public InquiryDetailsDto findByInquiryId(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ApplicationException(INQUIRY_NOT_FOUND));

        return InquiryMapper.toInquiryDetailsDto(inquiry);
    }

    public Page<InquiryDetailsForListDto> findInquiriesWithPagination(Pageable pageable) {
        return inquiryRepository.findAllWithPagination(pageable);
    }

    @Transactional
    public void update(
            String loginId,
            Long inquiryId,
            InquiryUpdateDto inquiryUpdateDto
    ) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ApplicationException(INQUIRY_NOT_FOUND));

        // 건의 작성자가 맞는지 검증
        if (!loginId.equals(inquiry.getUser().getLoginId())) {
            throw new ApplicationException(INQUIRY_WRITER_MISMATCH);
        }

        inquiry.update(
                inquiryUpdateDto.getTitle(),
                inquiryUpdateDto.getContent(),
                inquiryUpdateDto.getIsPublic()
        );
    }

    @Transactional
    public void delete(String loginId, Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ApplicationException(INQUIRY_NOT_FOUND));

        // 건의 작성자가 맞는지 검증
        if (!loginId.equals(inquiry.getUser().getLoginId())) {
            throw new ApplicationException(INQUIRY_WRITER_MISMATCH);
        }

        inquiryRepository.delete(inquiry);
    }
}
