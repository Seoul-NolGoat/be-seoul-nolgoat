package wad.seoul_nolgoat.service.kakaoMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import wad.seoul_nolgoat.exception.ApplicationException;
import wad.seoul_nolgoat.service.kakaoMap.dto.StoreAdditionalInfoDto;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

import java.util.Optional;

import static wad.seoul_nolgoat.exception.ErrorCode.ADDRESS_CONVERSION_FAILED;

@RequiredArgsConstructor
@Service
public class KakaoMapService {

    private static final String AUTHORIZATION = "Authorization";
    private static final String QUERY = "query";
    private static final String KAKAO_AUTHORIZATION_START = "KakaoAK ";
    private static final String DOCUMENTS_PATH = "documents";
    private static final String LATITUDE_PATH = "y";
    private static final String LONGITUDE_PATH = "x";
    private static final int FIRST_INDEX = 0;

    // fetchStoreAdditionalInfo
    private static final String RADIUS = "radius";
    private static final int RADIUS_VALUE = 50;
    private static final String CATEGORY_NAME_PATH = "category_name";
    private static final String PHONE_PATH = "phone";
    private static final String PLACE_URL_PATH = "place_url";

    // fetchStoreKakaoGrade
    private static final String BASIC_INFO_PATH = "basicInfo";
    private static final String FEEDBACK_PATH = "feedback";
    private static final String SCORE_SUM_PATH = "scoresum";
    private static final String SCORE_COUNT_PATH = "scorecnt";

    // fetchRoadAddress
    private static final String ROAD_ADDRESS_PATH = "road_address";
    private static final String ADDRESS_NAME_PATH = "address_name";

    // fetchLotAddress
    private static final String LOT_ADDRESS_PATH = "address";

    @Value("${kakao.api.key}")
    private String apiKey;

    @Value("${kakao.api.coordinate-url}")
    private String coordinateApiUrl;

    @Value("${kakao.api.store-info-url}")
    private String storeInfoApiUrl;

    @Value("${kakao.api.store-info-website-url}")
    private String storeInfoWebsiteUrl;

    @Value("${kakao.api.address-url}")
    private String addressApiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public Optional<CoordinateDto> fetchCoordinate(String address) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, KAKAO_AUTHORIZATION_START + apiKey);

        String url = UriComponentsBuilder.fromHttpUrl(coordinateApiUrl)
                .queryParam(QUERY, address)
                .build()
                .toUriString();

        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode documentsNode = rootNode.path(DOCUMENTS_PATH);

            if (documentsNode.isArray() && !documentsNode.isEmpty()) {
                JsonNode firstDocument = documentsNode.get(FIRST_INDEX);
                double latitude = firstDocument.path(LATITUDE_PATH).asDouble();
                double longitude = firstDocument.path(LONGITUDE_PATH).asDouble();

                return Optional.of(new CoordinateDto(latitude, longitude));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<StoreAdditionalInfoDto> fetchStoreAdditionalInfo(
            String name,
            Double longitude,
            Double latitude
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, KAKAO_AUTHORIZATION_START + apiKey);

        String url = UriComponentsBuilder.fromHttpUrl(storeInfoApiUrl)
                .queryParam(QUERY, name)
                .queryParam(LONGITUDE_PATH, longitude)
                .queryParam(LATITUDE_PATH, latitude)
                .queryParam(RADIUS, RADIUS_VALUE)
                .build()
                .toUriString();

        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode documentsNode = rootNode.path(DOCUMENTS_PATH);

            if (documentsNode.isArray() && !documentsNode.isEmpty()) {
                JsonNode firstDocument = documentsNode.get(FIRST_INDEX);
                String categoryName = firstDocument.path(CATEGORY_NAME_PATH).asText();
                String phone = firstDocument.path(PHONE_PATH).asText();
                String placeUrl = firstDocument.path(PLACE_URL_PATH).asText();

                return Optional.of(new StoreAdditionalInfoDto(categoryName, phone, placeUrl));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Double fetchStoreKakaoGrade(Long storeKakaoId) {
        String url = storeInfoWebsiteUrl + storeKakaoId;

        try {
            HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode feedbackNode = rootNode.path(BASIC_INFO_PATH).path(FEEDBACK_PATH);

            if (!feedbackNode.isMissingNode()) {
                int scoreSum = feedbackNode.path(SCORE_SUM_PATH).asInt();
                int scoreCount = feedbackNode.path(SCORE_COUNT_PATH).asInt();

                if (scoreCount != 0) {
                    return Math.round((double) scoreSum / scoreCount * 100) / 100.0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (double) 0;
    }

    public Optional<String> fetchRoadAddress(CoordinateDto coordinate) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, KAKAO_AUTHORIZATION_START + apiKey);

        String url = UriComponentsBuilder.fromHttpUrl(addressApiUrl)
                .queryParam(LONGITUDE_PATH, coordinate.getLongitude())
                .queryParam(LATITUDE_PATH, coordinate.getLatitude())
                .build()
                .toUriString();

        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode documentsNode = rootNode.path(DOCUMENTS_PATH);

            if (documentsNode.isArray() && !documentsNode.isEmpty()) {
                JsonNode firstDocument = documentsNode.get(FIRST_INDEX);
                JsonNode roadAddressNode = firstDocument.path(ROAD_ADDRESS_PATH);

                if (!roadAddressNode.isMissingNode()) {
                    return Optional.of(roadAddressNode.path(ADDRESS_NAME_PATH).asText());
                }
            }

            return Optional.empty();
        } catch (Exception e) {
            throw new ApplicationException(ADDRESS_CONVERSION_FAILED, e);
        }
    }

    public Optional<String> fetchLotAddress(CoordinateDto coordinate) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, KAKAO_AUTHORIZATION_START + apiKey);

        String url = UriComponentsBuilder.fromHttpUrl(addressApiUrl)
                .queryParam(LONGITUDE_PATH, coordinate.getLongitude())
                .queryParam(LATITUDE_PATH, coordinate.getLatitude())
                .build()
                .toUriString();

        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode documentsNode = rootNode.path(DOCUMENTS_PATH);

            if (documentsNode.isArray() && !documentsNode.isEmpty()) {
                JsonNode firstDocument = documentsNode.get(FIRST_INDEX);
                JsonNode roadAddressNode = firstDocument.path(LOT_ADDRESS_PATH);

                if (!roadAddressNode.isMissingNode()) {
                    return Optional.of(roadAddressNode.path(ADDRESS_NAME_PATH).asText());
                }
            }

            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
