package wad.seoul_nolgoat.web.store.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wad.seoul_nolgoat.domain.store.StoreType;
import wad.seoul_nolgoat.web.review.dto.response.ReviewDetailsForStoreDto;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class StoreDetailsDto {

    private final Long id;
    private final StoreType storeType;
    private final String name;
    private final String category;
    private final String managementNumber;
    private final String phoneNumber;
    private final String lotAddress;
    private final String roadAddress;
    private final double latitude;
    private final double longitude;
    private final double kakaoAverageGrade;
    private final double nolgoatAverageGrade;
    private final String placeUrl;
    private final List<ReviewDetailsForStoreDto> reviews;
}
