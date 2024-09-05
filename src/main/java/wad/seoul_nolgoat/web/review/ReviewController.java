package wad.seoul_nolgoat.web.review;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import wad.seoul_nolgoat.service.review.ReviewService;
import wad.seoul_nolgoat.web.review.dto.request.ReviewSaveDto;
import wad.seoul_nolgoat.web.review.dto.request.ReviewUpdateDto;
import wad.seoul_nolgoat.web.review.dto.response.ReviewDetailsForUserDto;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{userId}/{storeId}")
    public ResponseEntity<Void> createReview(
            @PathVariable Long userId,
            @PathVariable Long storeId,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("review") ReviewSaveDto reviewSaveDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long reviewId = reviewService.save(
                userId,
                storeId,
                Optional.ofNullable(file),
                reviewSaveDto
        );
        URI location = uriComponentsBuilder.path("/api/reviews/{reviewId}")
                .buildAndExpand(reviewId)
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ReviewDetailsForUserDto>> showReviewsByUserId(@PathVariable Long userId) {
        return ResponseEntity
                .ok(reviewService.findByUserId(userId));
    }

    // 현재 사용하지 않음
    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> update(@PathVariable Long reviewId, @RequestBody ReviewUpdateDto reviewUpdateDto) {
        reviewService.update(reviewId, reviewUpdateDto);

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long reviewId) {
        reviewService.deleteById(reviewId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
