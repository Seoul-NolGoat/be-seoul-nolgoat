package wad.seoul_nolgoat.domain.inquiry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wad.seoul_nolgoat.web.inquiry.dto.response.InquiryDetailsForListDto;

public interface InquiryRepositoryCustom {

    Page<InquiryDetailsForListDto> findAllWithPagination(Pageable pageable);
}
