package wad.seoul_nolgoat.service.search.sort;

import wad.seoul_nolgoat.service.search.dto.StoreForDistanceSortDto;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;
import wad.seoul_nolgoat.web.search.dto.response.CombinationDto;

public class DistanceCalculator {

    public static final double EARTH_RADIUS_KM = 6371.0;

    private static final int METER_CONVERSION_FACTOR = 1000;
    private static final double TO_RADIAN = Math.PI / 180;

    public static int calculateTotalDistance(
            StoreForDistanceSortDto firstStore,
            StoreForDistanceSortDto secondStore,
            StoreForDistanceSortDto thirdStore,
            CoordinateDto startCoordinate
    ) {
        CoordinateDto firstCoordinate = firstStore.getCoordinate();
        CoordinateDto secondCoordinate = secondStore.getCoordinate();
        CoordinateDto thirdCoordinate = thirdStore.getCoordinate();

        return (int) (
                (calculateDistance(startCoordinate, firstCoordinate)
                        + calculateDistance(firstCoordinate, secondCoordinate)
                        + calculateDistance(secondCoordinate, thirdCoordinate)) * METER_CONVERSION_FACTOR
        );
    }

    public static int calculateTotalDistance(
            StoreForDistanceSortDto firstStore,
            StoreForDistanceSortDto secondStore,
            CoordinateDto startCoordinate
    ) {
        CoordinateDto firstCoordinate = firstStore.getCoordinate();
        CoordinateDto secondCoordinate = secondStore.getCoordinate();

        return (int) (
                (calculateDistance(startCoordinate, firstCoordinate)
                        + calculateDistance(firstCoordinate, secondCoordinate)) * METER_CONVERSION_FACTOR
        );
    }

    public static int calculateTotalDistance(StoreForDistanceSortDto firstStore, CoordinateDto startCoordinate) {
        CoordinateDto firstCoordinate = firstStore.getCoordinate();

        return (int) (calculateDistance(startCoordinate, firstCoordinate) * METER_CONVERSION_FACTOR);
    }

    public static int calculateTotalDistanceForGradeWithFallback(
            int totalRounds,
            CombinationDto combinationDto,
            CoordinateDto startCoordinate
    ) {
        if (totalRounds == 3) {
            CoordinateDto firstCoordinate = combinationDto.getFirstStore().getCoordinate();
            CoordinateDto secondCoordinate = combinationDto.getSecondStore().getCoordinate();
            CoordinateDto thirdCoordinate = combinationDto.getThirdStore().getCoordinate();
            return (int) (
                    (calculateDistance(startCoordinate, firstCoordinate)
                            + calculateDistance(firstCoordinate, secondCoordinate)
                            + calculateDistance(secondCoordinate, thirdCoordinate)) * METER_CONVERSION_FACTOR
            );
        }
        if (totalRounds == 2) {
            CoordinateDto firstCoordinate = combinationDto.getFirstStore().getCoordinate();
            CoordinateDto secondCoordinate = combinationDto.getSecondStore().getCoordinate();
            return (int) (
                    (calculateDistance(startCoordinate, firstCoordinate)
                            + calculateDistance(firstCoordinate, secondCoordinate)) * METER_CONVERSION_FACTOR
            );
        }

        // 1차인 경우
        CoordinateDto firstCoordinate = combinationDto.getFirstStore().getCoordinate();
        return (int) (calculateDistance(startCoordinate, firstCoordinate) * METER_CONVERSION_FACTOR);
    }

    private static double calculateDistance(CoordinateDto firstCoordinate, CoordinateDto secondCoordinate) {
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
