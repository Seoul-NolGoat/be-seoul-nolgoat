package wad.seoul_nolgoat.service.tMap;

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
import wad.seoul_nolgoat.service.kakaoMap.dto.CoordinateDto;
import wad.seoul_nolgoat.service.tMap.dto.WalkRouteInfoDto;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class TMapService {

    private static final String START_NAME = "%EC%B6%9C%EB%B0%9C%EC%A7%80%EC%A0%90";
    private static final String END_NAME = "%EB%8F%84%EC%B0%A9%EC%A7%80%EC%A0%90";
    private static final String SEARCH_OPTION = "10";
    private static final String SORT = "custom";

    private static final String APPKEY = "appKey";

    private static final String FEATURES_PATH = "features";
    private static final String PROPERTIES_PATH = "properties";
    private static final String TOTAL_DISTANCE_PATH = "totalDistance";
    private static final String TOTAL_TIME_PATH = "totalTime";

    @Value("${t.api.key}")
    private String apiKey;

    @Value("${t.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public WalkRouteInfoDto fetchWalkRouteInfo(CoordinateDto startCoordinate, CoordinateDto endCoordinate) {
        try {
            Map<String, String> requestBodyMap = new HashMap<>();
            requestBodyMap.put("startX", String.valueOf(startCoordinate.getLongitude()));
            requestBodyMap.put("startY", String.valueOf(startCoordinate.getLatitude()));
            requestBodyMap.put("endX", String.valueOf(endCoordinate.getLongitude()));
            requestBodyMap.put("endY", String.valueOf(endCoordinate.getLatitude()));
            requestBodyMap.put("startName", START_NAME);
            requestBodyMap.put("endName", END_NAME);
            requestBodyMap.put("searchOption", SEARCH_OPTION);
            requestBodyMap.put("sort", SORT);

            String requestBody = objectMapper.writeValueAsString(requestBodyMap);

            HttpHeaders headers = new HttpHeaders();
            headers.set(APPKEY, apiKey);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode features = rootNode.path(FEATURES_PATH);

            for (JsonNode feature : features) {
                JsonNode properties = feature.path(PROPERTIES_PATH);
                if (properties.has(TOTAL_DISTANCE_PATH) && properties.has(TOTAL_TIME_PATH)) {
                    return new WalkRouteInfoDto(properties.get(TOTAL_DISTANCE_PATH).asInt(), properties.get(TOTAL_TIME_PATH).asInt());
                }
            }

            throw new RuntimeException("totalDistance not found in the response");
        } catch (Exception e) {
            throw new RuntimeException("TMap API error", e);
        }
    }
}
