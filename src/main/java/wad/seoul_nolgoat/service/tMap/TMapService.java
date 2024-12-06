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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.service.tMap.dto.WalkRouteInfoDto;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static wad.seoul_nolgoat.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class TMapService {

    public static final int INVALID_TIME = -1;

    private static final String START_NAME = "%EC%B6%9C%EB%B0%9C%EC%A7%80%EC%A0%90";
    private static final String END_NAME = "%EB%8F%84%EC%B0%A9%EC%A7%80%EC%A0%90";
    private static final String SEARCH_OPTION = "10";
    private static final String SORT = "custom";

    private static final String APPKEY = "appKey";

    private static final String FEATURES_PATH = "features";
    private static final String PROPERTIES_PATH = "properties";
    private static final String TOTAL_DISTANCE_PATH = "totalDistance";
    private static final String TOTAL_TIME_PATH = "totalTime";

    private static final String COORDINATE_SEPARATOR = ",";
    private static final String WAYPOINT_SEPARATOR = "_";

    @Value("${t.api.key}")
    private String apiKey;

    @Value("${t.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public WalkRouteInfoDto fetchWalkRouteInfo(CoordinateDto startCoordinate, CoordinateDto endCoordinate) {
        List<String> passList = new ArrayList<>();

        return fetchWalkRouteInfo(
                startCoordinate,
                endCoordinate,
                passList
        );
    }

    public WalkRouteInfoDto fetchWalkRouteInfo(
            CoordinateDto startCoordinate,
            CoordinateDto waypointCoordinate1,
            CoordinateDto endCoordinate
    ) {
        List<String> passList = new ArrayList<>();
        passList.add(convertCoordinateToString(waypointCoordinate1));

        return fetchWalkRouteInfo(
                startCoordinate,
                endCoordinate,
                passList
        );
    }

    public WalkRouteInfoDto fetchWalkRouteInfo(
            CoordinateDto startCoordinate,
            CoordinateDto waypointCoordinate1,
            CoordinateDto waypointCoordinate2,
            CoordinateDto endCoordinate
    ) {
        List<String> passList = new ArrayList<>();
        passList.add(convertCoordinateToString(waypointCoordinate1));
        passList.add(convertCoordinateToString(waypointCoordinate2));

        return fetchWalkRouteInfo(
                startCoordinate,
                endCoordinate,
                passList
        );
    }

    public WalkRouteInfoDto fetchWalkRouteInfo(
            CoordinateDto startCoordinate,
            CoordinateDto endCoordinate,
            List<String> passList
    ) {
        try {
            Map<String, String> requestBodyMap = new HashMap<>();
            requestBodyMap.put("startX", String.valueOf(startCoordinate.getLongitude()));
            requestBodyMap.put("startY", String.valueOf(startCoordinate.getLatitude()));
            requestBodyMap.put("endX", String.valueOf(endCoordinate.getLongitude()));
            requestBodyMap.put("endY", String.valueOf(endCoordinate.getLatitude()));

            if (!passList.isEmpty()) {
                requestBodyMap.put("passList", String.join(WAYPOINT_SEPARATOR, passList));
            }

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
            throw new ApiException(WALKING_DISTANCE_CALCULATION_FAILED);
        } catch (HttpClientErrorException.TooManyRequests e) {
            String body = e.getResponseBodyAsString();
            if (body.contains("QUOTA_EXCEEDED")) {
                throw new ApiException(API_QUOTA_EXCEEDED);
            }
            throw new ApiException(API_RATE_LIMIT_EXCEEDED);
        } catch (Exception e) {
            throw new ApiException(TMAP_API_CALL_FAILED);
        }
    }

    public WalkRouteInfoDto fetchFullPathWalkRouteInfo(
            CoordinateDto startCoordinate,
            CoordinateDto pass1,
            CoordinateDto pass2,
            CoordinateDto endCoordinate
    ) {
        if (isSameLocation(startCoordinate, pass1) && isSameLocation(pass1, pass2) && isSameLocation(pass2, endCoordinate)) {
            return new WalkRouteInfoDto(0, 0);
        }
        if (isSameLocation(startCoordinate, pass1) && isSameLocation(pass1, pass2)) {
            return fetchWalkRouteInfo(pass2, endCoordinate);
        }
        if (isSameLocation(pass1, pass2) && isSameLocation(pass2, endCoordinate)) {
            return fetchWalkRouteInfo(startCoordinate, pass1);
        }
        if (isSameLocation(startCoordinate, pass1) && isSameLocation(pass2, endCoordinate)) {
            return fetchWalkRouteInfo(pass1, pass2);
        }
        if (isSameLocation(startCoordinate, pass1)) {
            return fetchWalkRouteInfo(pass1, pass2, endCoordinate);
        }
        if (isSameLocation(pass2, endCoordinate)) {
            return fetchWalkRouteInfo(startCoordinate, pass1, pass2);
        }
        if (isSameLocation(pass1, pass2)) {
            WalkRouteInfoDto firstWalkRouteInfo = fetchWalkRouteInfo(startCoordinate, pass1);
            WalkRouteInfoDto secondWalkRouteInfo = fetchWalkRouteInfo(pass2, endCoordinate);

            return new WalkRouteInfoDto(
                    firstWalkRouteInfo.getTotalDistance() + secondWalkRouteInfo.getTotalDistance(),
                    firstWalkRouteInfo.getTotalTime() + secondWalkRouteInfo.getTotalTime()
            );
        }

        return fetchWalkRouteInfo(startCoordinate, pass1, pass2, endCoordinate);
    }

    public WalkRouteInfoDto fetchFullPathWalkRouteInfo(
            CoordinateDto startCoordinate,
            CoordinateDto pass,
            CoordinateDto endCoordinate
    ) {
        if (isSameLocation(startCoordinate, pass) && isSameLocation(pass, endCoordinate)) {
            return new WalkRouteInfoDto(0, 0);
        }
        if (isSameLocation(startCoordinate, pass)) {
            return fetchWalkRouteInfo(pass, endCoordinate);
        }
        if (isSameLocation(pass, endCoordinate)) {
            return fetchWalkRouteInfo(startCoordinate, pass);
        }

        return fetchWalkRouteInfo(startCoordinate, pass, endCoordinate);
    }

    public WalkRouteInfoDto fetchFullPathWalkRouteInfo(
            CoordinateDto startCoordinate,
            CoordinateDto endCoordinate
    ) {
        if (isSameLocation(startCoordinate, endCoordinate)) {
            return new WalkRouteInfoDto(0, 0);
        }

        return fetchWalkRouteInfo(startCoordinate, endCoordinate);
    }

    private String convertCoordinateToString(CoordinateDto coordinate) {
        return coordinate.getLongitude() + COORDINATE_SEPARATOR + coordinate.getLatitude();
    }

    private boolean isSameLocation(CoordinateDto coordinate1, CoordinateDto coordinate2) {
        return (coordinate1.getLongitude() == coordinate2.getLongitude())
                && (coordinate1.getLatitude() == coordinate2.getLatitude());
    }
}
