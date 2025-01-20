package wad.seoul_nolgoat.web.review;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import wad.seoul_nolgoat.service.review.ReviewService;
import wad.seoul_nolgoat.web.review.dto.request.ReviewSaveDto;
import wad.seoul_nolgoat.web.review.dto.request.ReviewUpdateDto;

import java.net.URI;

@Tag(name = "리뷰")
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 작성")
    @PostMapping("/{storeId}")
    public ResponseEntity<Void> createReview(
            @AuthenticationPrincipal OAuth2User loginUser,
            @PathVariable Long storeId,
            @RequestPart(value = "file", required = false) MultipartFile image,
            @Valid @RequestPart("review") ReviewSaveDto reviewSaveDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long reviewId = reviewService.save(
                loginUser.getName(),
                storeId,
                image,
                reviewSaveDto
        );
        URI location = uriComponentsBuilder.path("/api/reviews/{reviewId}")
                .buildAndExpand(reviewId)
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }

    @Hidden
    // 현재 사용하지 않음
    @PatchMapping("/{reviewId}")
    public ResponseEntity<Void> update(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateDto reviewUpdateDto
    ) {
        reviewService.update(reviewId, reviewUpdateDto);

        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(summary = "리뷰 삭제")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal OAuth2User loginUser,
            @PathVariable Long reviewId
    ) {
        reviewService.delete(loginUser.getName(), reviewId);

        return ResponseEntity
                .noContent()
                .build();
    }
}