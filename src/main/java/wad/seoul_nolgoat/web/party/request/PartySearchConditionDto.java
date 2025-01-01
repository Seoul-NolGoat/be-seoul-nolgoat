package wad.seoul_nolgoat.web.party.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PartySearchConditionDto {

    private String status; // opened or closed or null
    private String district; // 구역

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    @Builder.Default
    private String sortField = "createdDate"; // 정렬 대상
}
