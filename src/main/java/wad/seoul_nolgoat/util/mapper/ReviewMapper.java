package wad.seoul_nolgoat.util.mapper;

import wad.seoul_nolgoat.domain.review.Review;
import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.web.review.dto.request.ReviewSaveDto;
import wad.seoul_nolgoat.web.review.dto.response.ReviewDetailsForStoreDto;
import wad.seoul_nolgoat.web.review.dto.response.ReviewDetailsForUserDto;

import java.util.Optional;

public class ReviewMapper {

    public static Review toEntity(
            User user,
            Store store,
            Optional<String> optionalImageUrl,
            ReviewSaveDto reviewSaveDto) {
        if (optionalImageUrl.isPresent()) {
            return new Review(
                    reviewSaveDto.getGrade(),
                    reviewSaveDto.getContent(),
                    optionalImageUrl.get(),
                    user,
                    store
            );
        }

        return new Review(
                reviewSaveDto.getGrade(),
                reviewSaveDto.getContent(),
                user,
                store
        );
    }

    public static ReviewDetailsForUserDto toReviewDetailsForUserDto(Review review) {
        return new ReviewDetailsForUserDto(
                review.getGrade(),
                review.getContent(),
                review.getStore().getId(),
                review.getStore().getName()
        );
    }

    public static ReviewDetailsForStoreDto toReviewDetailsForStoreDto(Review review) {
        return new ReviewDetailsForStoreDto(
                review.getId(),
                review.getGrade(),
                review.getContent(),
                review.getUser().getNickname(),
                review.getUser().getProfileImage()
        );
    }
}
