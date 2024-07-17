package wad.seoul_nolgoat.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.domain.review.Review;
import wad.seoul_nolgoat.domain.review.ReviewRepository;
import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.domain.store.StoreRepository;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.util.mapper.ReviewMapper;
import wad.seoul_nolgoat.web.review.dto.request.ReviewSaveDto;
import wad.seoul_nolgoat.web.review.dto.request.ReviewUpdateDto;
import wad.seoul_nolgoat.web.review.dto.response.ReviewDetailsDto;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public Long save(
            Long userId,
            Long storeId,
            ReviewSaveDto reviewSaveDto) {
        User user = userRepository.findById(userId).get();
        Store store = storeRepository.findById(storeId).get();

        return reviewRepository.save(
                ReviewMapper.toEntity(
                        user,
                        store,
                        reviewSaveDto
                )
        ).getId();
    }

    public List<ReviewDetailsDto> findByUserId(Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);

        return reviews.stream()
                .map(ReviewMapper::toReviewDetailsDto)
                .toList();
    }

    @Transactional
    public void update(Long reviewId, ReviewUpdateDto reviewUpdateDto) {
        Review review = reviewRepository.findById(reviewId).get();
        review.update(reviewUpdateDto.getGrade(), reviewUpdateDto.getContent());
    }

    public void deleteById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).get();
        review.delete();
        reviewRepository.deleteById(reviewId);
    }
}