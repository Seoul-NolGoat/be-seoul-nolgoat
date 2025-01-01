package wad.seoul_nolgoat.web.party.request;

import lombok.Getter;

@Getter
public class PartySearchConditionDto {

    private String status; // 전체 or 모집 중 or 마감
    private String district; // 지역(행정구역)
    private int page;
    private int size;
    private String sortField; // 정렬 대상
}
