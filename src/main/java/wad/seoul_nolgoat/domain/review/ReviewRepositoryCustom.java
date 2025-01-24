package wad.seoul_nolgoat.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wad.seoul_nolgoat.web.review.dto.response.ReviewDetailsForUserDto;

public interface ReviewRepositoryCustom {

    Page<ReviewDetailsForUserDto> findReviewDetailsByLoginId(String loginId, Pageable pageable);
}
