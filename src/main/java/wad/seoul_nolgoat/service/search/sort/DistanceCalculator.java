package wad.seoul_nolgoat.service.search.sort;

import org.springframework.stereotype.Component;
import wad.seoul_nolgoat.service.search.dto.StoreForDistanceSortDto;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

@Component
public class DistanceCalculator {

    public static final double EARTH_RADIUS_KM = 6371.0;

    private static final double TO_RADIAN = Math.PI / 180;

    public double calculateTotalDistance(
            StoreForDistanceSortDto firstStore,
            StoreForDistanceSortDto secondStore,
            StoreForDistanceSortDto thirdStore,
            CoordinateDto startCoordinate
    ) {
        CoordinateDto firstCoordinate = firstStore.getCoordinate();
        CoordinateDto secondCoordinate = secondStore.getCoordinate();
        CoordinateDto thirdCoordinate = thirdStore.getCoordinate();

        return calculateDistance(startCoordinate, firstCoordinate)
                + calculateDistance(firstCoordinate, secondCoordinate)
                + calculateDistance(secondCoordinate, thirdCoordinate);
    }

    public double calculateTotalDistance(
            StoreForDistanceSortDto firstStore,
            StoreForDistanceSortDto secondStore,
            CoordinateDto startCoordinate
    ) {
        CoordinateDto firstCoordinate = firstStore.getCoordinate();
        CoordinateDto secondCoordinate = secondStore.getCoordinate();

        return calculateDistance(startCoordinate, firstCoordinate)
                + calculateDistance(firstCoordinate, secondCoordinate);
    }

    public double calculateTotalDistance(StoreForDistanceSortDto firstStore, CoordinateDto startCoordinate) {
        CoordinateDto firstCoordinate = firstStore.getCoordinate();

        return calculateDistance(startCoordinate, firstCoordinate);
    }

    private double calculateDistance(CoordinateDto firstCoordinate, CoordinateDto secondCoordinate) {
        double firstLatitude = firstCoordinate.getLatitude();
        double firstLongitude = firstCoordinate.getLongitude();
        double secondLatitude = secondCoordinate.getLatitude();
        double secondLongitude = secondCoordinate.getLongitude();

        double sinDeltaLatitude = Math.sin(Math.abs(firstLatitude - secondLatitude) * TO_RADIAN / 2);
        double sinDeltaLongitude = Math.sin(Math.abs(firstLongitude - secondLongitude) * TO_RADIAN / 2);

        double squareRoot = Math.sqrt(
                sinDeltaLatitude
                        * sinDeltaLatitude
                        + Math.cos(firstLatitude * TO_RADIAN)
                        * Math.cos(secondLatitude * TO_RADIAN)
                        * sinDeltaLongitude
                        * sinDeltaLongitude
        );

        return EARTH_RADIUS_KM * Math.asin(squareRoot) * 2;
    }
}
