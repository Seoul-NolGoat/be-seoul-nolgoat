package wad.seoul_nolgoat.web.review;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import wad.seoul_nolgoat.service.review.ReviewService;
import wad.seoul_nolgoat.util.ValidationUtil;
import wad.seoul_nolgoat.web.review.dto.request.ReviewSaveDto;
import wad.seoul_nolgoat.web.review.dto.request.ReviewUpdateDto;
import wad.seoul_nolgoat.web.review.dto.response.ReviewDetailsForUserDto;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{userId}/{storeId}")
    public ResponseEntity<?> createReview(
            @PathVariable Long userId,
            @PathVariable Long storeId,
            @Valid @RequestBody ReviewSaveDto reviewSaveDto,
            BindingResult bindingResult,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(ValidationUtil.extractErrors(bindingResult));
        }
        Long reviewId = reviewService.save(
                userId,
                storeId,
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

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> update(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateDto reviewUpdateDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(ValidationUtil.extractErrors(bindingResult));
        }
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
