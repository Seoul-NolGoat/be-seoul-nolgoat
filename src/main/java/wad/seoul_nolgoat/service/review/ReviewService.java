package wad.seoul_nolgoat.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wad.seoul_nolgoat.domain.review.Review;
import wad.seoul_nolgoat.domain.review.ReviewRepository;
import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.domain.store.StoreRepository;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.ApplicationException;
import wad.seoul_nolgoat.service.s3.S3Service;
import wad.seoul_nolgoat.util.mapper.ReviewMapper;
import wad.seoul_nolgoat.web.review.dto.request.ReviewSaveDto;
import wad.seoul_nolgoat.web.review.dto.request.ReviewUpdateDto;
import wad.seoul_nolgoat.web.review.dto.response.ReviewDetailsForUserDto;

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
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));

        if (reviewRepository.existsByUserIdAndStoreId(user.getId(), storeId)) {
            throw new ApplicationException(DUPLICATE_REVIEW);
        }

        Optional<String> imageUrl = Optional.ofNullable(image)
                .filter(file -> !file.isEmpty())
                .map(s3Service::saveFile);

        store.addNolgoatGrade(reviewSaveDto.grade());

        return reviewRepository.save(
                ReviewMapper.toEntity(
                        user,
                        store,
                        imageUrl.orElse(null),
                        reviewSaveDto
                )
        ).getId();
    }

    public Page<ReviewDetailsForUserDto> findReviewDetailsByLoginId(String loginId, Pageable pageable) {
        return reviewRepository.findReviewDetailsByLoginId(loginId, pageable);
    }

    @Transactional
    public void update(Long reviewId, ReviewUpdateDto reviewUpdateDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApplicationException(REVIEW_NOT_FOUND));

        int previousNolgoatGrade = review.getGrade();
        int currentNolgoatGrade = reviewUpdateDto.grade();
        int nolgoatGradeDifference = previousNolgoatGrade - currentNolgoatGrade;

        Store store = review.getStore();
        store.updateNolgoatAverageGradeForEditReview(nolgoatGradeDifference);

        review.update(
                currentNolgoatGrade,
                reviewUpdateDto.content()
        );
    }

    @Transactional
    public void delete(String loginId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApplicationException(REVIEW_NOT_FOUND));

        if (!loginId.equals(review.getUser().getLoginId())) {
            throw new ApplicationException(REVIEW_WRITER_MISMATCH);
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