package wad.seoul_nolgoat.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wad.seoul_nolgoat.domain.review.Review;
import wad.seoul_nolgoat.domain.review.ReviewRepository;
import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.domain.store.StoreRepository;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.service.s3.S3Service;
import wad.seoul_nolgoat.util.mapper.ReviewMapper;
import wad.seoul_nolgoat.web.review.dto.request.ReviewSaveDto;
import wad.seoul_nolgoat.web.review.dto.request.ReviewUpdateDto;
import wad.seoul_nolgoat.web.review.dto.response.ReviewDetailsForUserDto;

import java.util.List;
import java.util.Optional;

import static wad.seoul_nolgoat.exception.ErrorCode.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final S3Service s3Service;

    @Transactional
    public Long save(
            String loginId,
            Long storeId,
            MultipartFile image,
            ReviewSaveDto reviewSaveDto
    ) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApiException(STORE_NOT_FOUND));

        if (reviewRepository.existsByUserIdAndStoreId(user.getId(), storeId)) {
            throw new ApiException(DUPLICATE_REVIEW);
        }

        Optional<String> imageUrl = Optional.ofNullable(image)
                .filter(file -> !file.isEmpty())
                .map(s3Service::saveFile);

        store.addNolgoatGrade(reviewSaveDto.getGrade());

        return reviewRepository.save(
                ReviewMapper.toEntity(
                        user,
                        store,
                        imageUrl.orElse(null),
                        reviewSaveDto
                )
        ).getId();
    }

    public List<ReviewDetailsForUserDto> findByUserId(Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);

        return reviews.stream()
                .map(ReviewMapper::toReviewDetailsForUserDto)
                .toList();
    }

    @Transactional
    public void update(Long reviewId, ReviewUpdateDto reviewUpdateDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(REVIEW_NOT_FOUND));

        int previousNolgoatGrade = review.getGrade();
        int currentNolgoatGrade = reviewUpdateDto.getGrade();
        int nolgoatGradeDifference = previousNolgoatGrade - currentNolgoatGrade;

        Store store = review.getStore();
        store.updateNolgoatAverageGradeForEditReview(nolgoatGradeDifference);

        review.update(
                currentNolgoatGrade,
                reviewUpdateDto.getContent()
        );
    }

    @Transactional
    public void delete(String loginId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(REVIEW_NOT_FOUND));

        if (!loginId.equals(review.getUser().getLoginId())) {
            throw new ApiException(REVIEW_WRITER_MISMATCH);
        }

        Store store = review.getStore();
        store.updateNolgoatAverageGradeForDeleteReview(review.getGrade());

        // s3 이미지 파일 삭제
        if (review.hasImageUrl()) {
            s3Service.deleteFile(review.getImageUrl());
        }

        review.delete();
        reviewRepository.delete(review);
    }
}