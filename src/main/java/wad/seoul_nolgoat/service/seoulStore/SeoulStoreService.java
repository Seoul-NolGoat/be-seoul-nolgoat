package wad.seoul_nolgoat.service.seoulStore;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.domain.store.StoreType;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
public class SeoulStoreService {

    private static final String METHOD_GET = "GET";
    private static final String ROW_PATH = "row";
    public static final String STORE_OPERATION_STATUS_PATH = "TRDSTATEGBN";
    public static final String STORE_STATUS_OPEN = "01";
    public static final String MANAGEMENT_NUMBER_PATH = "MGTNO";
    public static final String LOT_ADDRESS_PATH = "SITEWHLADDR";
    public static final String ROAD_ADDRESS_PATH = "RDNWHLADDR";
    public static final String STORE_NAME_PATH = "BPLCNM";

    @Value("${seoul.api.url}")
    private String apiUrl;

    @Value("${seoul.api.key}")
    private String apiKey;

    @Value("${seoul.api.file-format}")
    private String fileFormat;

    @Value("${seoul.api.code-restaurant}")
    private String restaurantCode;

    @Value("${seoul.api.code-cafe}")
    private String cafeCode;

    @Value("${seoul.api.code-pcroom}")
    private String pcroomCode;

    @Value("${seoul.api.code-karaoke}")
    private String karaokeCode;

    @Value("${seoul.api.code-billiard}")
    private String billiardCode;

    private final Map<StoreType, String> storeTypeCodeMap = new HashMap<>();

    @PostConstruct
    public void init() {
        storeTypeCodeMap.put(StoreType.RESTAURANT, restaurantCode);
        storeTypeCodeMap.put(StoreType.CAFE, cafeCode);
        storeTypeCodeMap.put(StoreType.PCROOM, pcroomCode);
        storeTypeCodeMap.put(StoreType.KARAOKE, karaokeCode);
        storeTypeCodeMap.put(StoreType.BILLIARD, billiardCode);
    }

    public Optional<List<Store>> fetchSeoulStoreInfo(StoreType storeType, int startIdx, int endIdx) {
        try {
            String code = storeTypeCodeMap.get(storeType);

            String url = String.format("%s/%s/%s/%s/%d/%d/", apiUrl, apiKey, fileFormat, code, startIdx, endIdx);
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod(METHOD_GET);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(conn.getInputStream());

            JsonNode rows = rootNode.path(code).path(ROW_PATH);
            if (rows.isMissingNode()) {
                return Optional.empty();
            }

            List<Store> stores = new ArrayList<>();

            for (JsonNode row : rows) {
                if (STORE_STATUS_OPEN.equals(row.path(STORE_OPERATION_STATUS_PATH).asText())) {
                    Store store = Store.builder()
                            .storeType(storeType)
                            .name(row.path(STORE_NAME_PATH).asText())
                            .managementNumber(row.path(MANAGEMENT_NUMBER_PATH).asText())
                            .lotAddress(row.path(LOT_ADDRESS_PATH).asText())
                            .roadAddress(row.path(ROAD_ADDRESS_PATH).asText())
                            .build();

                    stores.add(store);
                }
            }
            return Optional.of(stores);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
