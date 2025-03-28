package wad.seoul_nolgoat.web.review.dto.response;

public record ReviewDetailsForUserDto(
        Long reviewId,
        int grade,
        String content,
        String imageUrl,
        Long storeId,
        String storeName
) {
}