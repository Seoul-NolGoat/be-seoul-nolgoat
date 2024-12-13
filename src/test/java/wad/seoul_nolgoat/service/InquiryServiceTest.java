package wad.seoul_nolgoat.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.service.inquiry.InquiryService;
import wad.seoul_nolgoat.web.inquiry.dto.request.InquiryUpdateDto;
import wad.seoul_nolgoat.web.inquiry.dto.response.InquiryListDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wad.seoul_nolgoat.exception.ErrorCode.INQUIRY_WRITER_MISMATCH;

@ActiveProfiles("test")
@SpringBootTest
public class InquiryServiceTest {

    @Autowired
    private InquiryService inquiryService;

    @DisplayName("건의 사항 조회에 페이지네이션이 올바르게 적용되는지 확인합니다.")
    @Test
    void verify_inquiries_find_with_pagination() {
        // given
        Pageable firstPageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdDate"));
        Pageable secondPageable = PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, "createdDate"));
        Pageable thirdPageable = PageRequest.of(2, 2, Sort.by(Sort.Direction.DESC, "createdDate"));

        // when
        Page<InquiryListDto> firstPage = inquiryService.findInquiriesWithPagination(firstPageable);
        Page<InquiryListDto> secondPage = inquiryService.findInquiriesWithPagination(secondPageable);
        Page<InquiryListDto> thirdPage = inquiryService.findInquiriesWithPagination(thirdPageable);

        // then
        // 전체 페이지수 검증
        assertThat(firstPage.getTotalPages()).isEqualTo(3);

        // 1페이지 검증
        assertThat(firstPage.getContent().size()).isEqualTo(2);

        // 2페이지 검증
        assertThat(secondPage.getContent().size()).isEqualTo(2);

        // 3페이지 검증
        assertThat(thirdPage.getContent().size()).isEqualTo(1);
    }

    @DisplayName("건의 사항 작성자가 아닌 사용자가 글을 수정하려 하면 예외가 발생합니다.")
    @Test
    void update_inquiry_when_non_creator_then_throw_exception() {
        // given
        String loginId = "user2";
        Long inquiryId = 1L;
        InquiryUpdateDto updateDto = new InquiryUpdateDto("변경된 제목", "변경된 내용", true);

        // when // then
        assertThatThrownBy(() -> inquiryService.update(loginId, inquiryId, updateDto))
                .isInstanceOf(ApiException.class)
                .hasMessage(INQUIRY_WRITER_MISMATCH.getMessage());
    }

    @DisplayName("건의 사항 작성자가 아닌 사용자가 글을 삭제하려 하면 예외가 발생합니다.")
    @Test
    void delete_inquiry_when_non_creator_then_throw_exception() {
        // given
        String loginId = "user2";
        Long inquiryId = 1L;

        // when // then
        assertThatThrownBy(() -> inquiryService.delete(loginId, inquiryId))
                .isInstanceOf(ApiException.class)
                .hasMessage(INQUIRY_WRITER_MISMATCH.getMessage());
    }
}
