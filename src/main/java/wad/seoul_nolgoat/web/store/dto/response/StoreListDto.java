package wad.seoul_nolgoat.web.store.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StoreListDto {

    private final Long id;
    private final String name;
    private final String category;
    private final String phoneNumber;
    private final String roadAddress;
    private final double kakaoAverageGrade;
    private final double nolgoatAverageGrade;
}

