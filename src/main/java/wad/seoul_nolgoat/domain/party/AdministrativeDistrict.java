package wad.seoul_nolgoat.domain.party;

import wad.seoul_nolgoat.exception.ApplicationException;

import static wad.seoul_nolgoat.exception.ErrorCode.INVALID_ADMINISTRATIVE_DISTRICT;

public enum AdministrativeDistrict {

    GANGNAM_GU("강남구"),
    GANGDONG_GU("강동구"),
    GANGSEO_GU("강서구"),
    GANGBUK_GU("강북구"),
    GWANAK_GU("관악구"),
    GWANGJIN_GU("광진구"),
    GURO_GU("구로구"),
    GEUMCHEON_GU("금천구"),
    NOWON_GU("노원구"),
    DOBONG_GU("도봉구"),
    DONGDAEMUN_GU("동대문구"),
    DONGJAK_GU("동작구"),
    MAPO_GU("마포구"),
    SEODAEMUN_GU("서대문구"),
    SEOCHO_GU("서초구"),
    SEONGDONG_GU("성동구"),
    SEONGBUK_GU("성북구"),
    SONGPA_GU("송파구"),
    YANGCHEON_GU("양천구"),
    YEONGDEUNGPO_GU("영등포구"),
    YONGSAN_GU("용산구"),
    EUNPYEONG_GU("은평구"),
    JONGNO_GU("종로구"),
    JUNG_GU("중구"),
    JUNGNANG_GU("중랑구");

    private final String displayName;

    AdministrativeDistrict(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static AdministrativeDistrict fromString(String district) {
        if (district == null) {
            return null;
        }
        try {
            return AdministrativeDistrict.valueOf(district.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(INVALID_ADMINISTRATIVE_DISTRICT);
        }
    }
}
