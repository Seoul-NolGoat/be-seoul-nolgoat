package wad.seoul_nolgoat.domain.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wad.seoul_nolgoat.web.notice.dto.response.NoticeDetailsForListDto;

public interface NoticeRepositoryCustom {

    Page<NoticeDetailsForListDto> findAllWithPagination(Pageable pageable);
}
