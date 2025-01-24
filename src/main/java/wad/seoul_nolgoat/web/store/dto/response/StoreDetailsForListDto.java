package wad.seoul_nolgoat.web.store.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StoreDetailsForListDto {

    private final Long storeId;
    private final String name;
    private final String category;
    private final String phoneNumber;
    private final String roadAddress;
    private final double kakaoAverageGrade;
    private final double nolgoatAverageGrade;
}

