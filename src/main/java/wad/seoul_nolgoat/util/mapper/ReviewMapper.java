package wad.seoul_nolgoat.util.mapper;

import wad.seoul_nolgoat.domain.review.Review;
import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.web.review.dto.request.ReviewSaveDto;
import wad.seoul_nolgoat.web.review.dto.response.ReviewDetailsDto;

public class ReviewMapper {

    public static Review toEntity(
            User user,
            Store store,
            ReviewSaveDto reviewSaveDto) {
        return new Review(
                reviewSaveDto.getGrade(),
                reviewSaveDto.getContent(),
                user,
                store
        );
    }

    public static ReviewDetailsDto toReviewDetailsDto(Review review) {
        return new ReviewDetailsDto(
                review.getGrade(),
                review.getContent(),
                review.getUser().getId(),
                review.getStore().getId()
        );
    }
}
