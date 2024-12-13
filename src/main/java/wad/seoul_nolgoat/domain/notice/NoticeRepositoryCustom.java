package wad.seoul_nolgoat.domain.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wad.seoul_nolgoat.web.notice.dto.response.NoticeListDto;

public interface NoticeRepositoryCustom {

    Page<NoticeListDto> findAllWithPagination(Pageable pageable);
}
