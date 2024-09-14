package wad.seoul_nolgoat.domain.store;

public enum StoreType {

    RESTAURANT,
    CAFE,
    PCROOM,
    KARAOKE,
    BILLIARD;

    public static StoreType getStoreTypeByName(String storeTypeName) {
        return StoreType.valueOf(storeTypeName.toUpperCase());
    }
}
