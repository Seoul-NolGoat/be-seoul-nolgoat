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
import wad.seoul_nolgoat.service.notice.NoticeService;
import wad.seoul_nolgoat.web.notice.dto.response.NoticeListDto;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class NoticeServiceTest {

    @Autowired
    private NoticeService noticeService;

    @DisplayName("공지 사항 조회에 페이지네이션이 올바르게 적용되는지 확인합니다.")
    @Test
    void verify_notices_find_with_pagination() {
        // given
        Pageable firstPageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdDate"));
        Pageable secondPageable = PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, "createdDate"));
        Pageable thirdPageable = PageRequest.of(2, 2, Sort.by(Sort.Direction.DESC, "createdDate"));

        // when
        Page<NoticeListDto> firstPage = noticeService.findNoticesWithPagination(firstPageable);
        Page<NoticeListDto> secondPage = noticeService.findNoticesWithPagination(secondPageable);
        Page<NoticeListDto> thirdPage = noticeService.findNoticesWithPagination(thirdPageable);

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
}
