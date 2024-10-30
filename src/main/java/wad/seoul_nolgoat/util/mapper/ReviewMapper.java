package wad.seoul_nolgoat.util.mapper;

import wad.seoul_nolgoat.domain.review.Review;
import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.web.review.dto.request.ReviewSaveDto;
import wad.seoul_nolgoat.web.review.dto.response.ReviewDetailsForStoreDto;
import wad.seoul_nolgoat.web.review.dto.response.ReviewDetailsForUserDto;

import static wad.seoul_nolgoat.util.DateTimeUtil.formatDate;

public class ReviewMapper {

    public static Review toEntity(
            User user,
            Store store,
            String imageUrl,
            ReviewSaveDto reviewSaveDto
    ) {
        return new Review(
                reviewSaveDto.getGrade(),
                reviewSaveDto.getContent(),
                imageUrl,
                user,
                store
        );
    }

    public static ReviewDetailsForUserDto toReviewDetailsForUserDto(Review review) {
        return new ReviewDetailsForUserDto(
                review.getGrade(),
                review.getContent(),
                review.getImageUrl(),
                review.getStore().getId(),
                review.getStore().getName()
        );
    }

    public static ReviewDetailsForStoreDto toReviewDetailsForStoreDto(Review review) {
        return new ReviewDetailsForStoreDto(
                review.getId(),
                review.getGrade(),
                review.getContent(),
                review.getImageUrl(),
                review.getUser().getId(),
                review.getUser().getNickname(),
                review.getUser().getProfileImage(),
                formatDate(review.getCreatedDate())
        );
    }
}