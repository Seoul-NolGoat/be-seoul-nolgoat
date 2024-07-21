package wad.seoul_nolgoat.utilty;

import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.service.search.sort.CombinationForDistance;

import java.util.List;

public class DistanceCalculator {

    public static final double RADIUS = 6371; //지구 반지름(km)
    public static final double TO_RADIAN = Math.PI / 180;

    public double calculateDistance(CombinationForDistance combination) {

        List<Store> stores = combination.getStores();
        double totalDistance = 0;

        // 두 가게 사이의 거리를 계산하여 totalDistance에 더함
        for (int i = 0; i < stores.size() - 1; i++) {

            //인자로 받는 걸로 변경하기

            totalDistance += calculate(
                    stores.get(i).getLatitude(),
                    stores.get(i).getLongitude(),
                    stores.get(i + 1).getLatitude(),
                    stores.get(i + 1).getLongitude());
        }

        return totalDistance;
    }

    private double calculate(
            double firstLatitude,
            double firstLongitude,
            double secondLatitude,
            double secondLongitude) {
        double deltaLatitude = Math.abs(firstLatitude - secondLatitude) * TO_RADIAN;
        double deltaLongitude = Math.abs(firstLongitude - secondLongitude) * TO_RADIAN;

        double sinDeltaLatitude = Math.sin(deltaLatitude / 2);
        double sinDeltaLongitude = Math.sin(deltaLongitude / 2);

        double squareRoot = Math.sqrt(
                sinDeltaLatitude * sinDeltaLatitude +
                        Math.cos(firstLatitude * TO_RADIAN) *
                                Math.cos(secondLatitude * TO_RADIAN) *
                                sinDeltaLongitude * sinDeltaLongitude);

        double distance = 2 * RADIUS * Math.asin(squareRoot);
        return distance;
    }
}
