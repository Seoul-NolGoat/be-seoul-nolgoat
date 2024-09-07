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
import wad.seoul_nolgoat.exception.ErrorMessages;
import wad.seoul_nolgoat.exception.notfound.ReviewNotFoundException;
import wad.seoul_nolgoat.exception.notfound.StoreNotFoundException;
import wad.seoul_nolgoat.exception.notfound.UserNotFoundException;
import wad.seoul_nolgoat.service.s3.S3Service;
import wad.seoul_nolgoat.service.store.StoreService;
import wad.seoul_nolgoat.util.mapper.ReviewMapper;
import wad.seoul_nolgoat.web.review.dto.request.ReviewSaveDto;
import wad.seoul_nolgoat.web.review.dto.request.ReviewUpdateDto;
import wad.seoul_nolgoat.web.review.dto.response.ReviewDetailsForUserDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final StoreService storeService;
    private final S3Service s3Service;

    public Long save(
            Long userId,
            Long storeId,
            Optional<MultipartFile> optionalMultipartFile,
            ReviewSaveDto reviewSaveDto
    ) {
        if (reviewRepository.existsByUserIdAndStoreId(userId, storeId)) {
            throw new RuntimeException("하나의 상점에 한사람 당 한개의 리뷰만 작성 가능");
        }

        Optional<String> optionalImageUrl = Optional.empty();
        if (optionalMultipartFile.isPresent()) {
            try {
                optionalImageUrl = Optional.of(s3Service.saveFile(optionalMultipartFile.get()));
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }

        // accommodation averageGrade 업데이트
        storeService.updateAverageGradeOnReviewAdd(storeId, reviewSaveDto.getGrade());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorMessages.USER_NOT_FOUND_MESSAGE));
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(ErrorMessages.STORE_NOT_FOUND_MESSAGE));

        return reviewRepository.save(
                ReviewMapper.toEntity(
                        user,
                        store,
                        optionalImageUrl,
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
                .orElseThrow(() -> new ReviewNotFoundException(ErrorMessages.REVIEW_NOT_FOUND_MESSAGE));
        review.update(
                reviewUpdateDto.getGrade(),
                reviewUpdateDto.getContent(),
                reviewUpdateDto.getImageUrl()
        );
    }

    public void deleteById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(ErrorMessages.REVIEW_NOT_FOUND_MESSAGE));

        // accommodation averageGrade 업데이트
        storeService.updateAverageGradeOnReviewDelete(review.getStore().getId(), review.getGrade());
        // s3 이미지 파일 삭제
        if (review.hasImageUrl()) {
            s3Service.deleteFile(review.getImageUrl());
        }

        review.delete();
        reviewRepository.deleteById(reviewId);
    }
}