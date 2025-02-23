package wad.seoul_nolgoat.web.store.dto.response;

public record StoreDetailsForListDto(
        Long storeId,
        String name,
        String category,
        String phoneNumber,
        String roadAddress,
        double kakaoAverageGrade,
        double nolgoatAverageGrade
) {
}