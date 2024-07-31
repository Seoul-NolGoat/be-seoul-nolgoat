package wad.seoul_nolgoat.service.search.sort;

import wad.seoul_nolgoat.service.search.dto.StoreForDistanceSortDto;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

public class DistanceCalculator {

    private static final double RADIUS = 6371; // 상수 통합 예정
    private static final double TO_RADIAN = Math.PI / 180;

    public double calculateTotalDistance(
            StoreForDistanceSortDto firstStore,
            StoreForDistanceSortDto secondStore,
            StoreForDistanceSortDto thirdStore,
            CoordinateDto startCoordinate) {
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
            CoordinateDto startCoordinate) {
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

        return RADIUS * Math.asin(squareRoot) * 2;
    }
}
