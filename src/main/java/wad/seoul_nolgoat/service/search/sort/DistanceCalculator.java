package wad.seoul_nolgoat.service.search.sort;

import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.service.search.dto.StoreForDistanceSortDto;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;
import wad.seoul_nolgoat.web.search.dto.response.CombinationDto;

import static wad.seoul_nolgoat.exception.ErrorCode.INVALID_GATHERING_ROUND;

public class DistanceCalculator {

    public static final double EARTH_RADIUS_KM = 6371.0;

    private static final double TO_RADIAN = Math.PI / 180;

    public static double calculateTotalDistance(
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

    public static double calculateTotalDistance(
            StoreForDistanceSortDto firstStore,
            StoreForDistanceSortDto secondStore,
            CoordinateDto startCoordinate
    ) {
        CoordinateDto firstCoordinate = firstStore.getCoordinate();
        CoordinateDto secondCoordinate = secondStore.getCoordinate();

        return calculateDistance(startCoordinate, firstCoordinate)
                + calculateDistance(firstCoordinate, secondCoordinate);
    }

    public static double calculateTotalDistance(StoreForDistanceSortDto firstStore, CoordinateDto startCoordinate) {
        CoordinateDto firstCoordinate = firstStore.getCoordinate();

        return calculateDistance(startCoordinate, firstCoordinate);
    }

    public static double calculateTotalDistanceForGradeWithFallback(
            int totalRounds,
            CombinationDto combinationDto,
            CoordinateDto startCoordinate
    ) {
        if (totalRounds == 3) {
            CoordinateDto firstCoordinate = combinationDto.getFirstStore().getCoordinate();
            CoordinateDto secondCoordinate = combinationDto.getSecondStore().getCoordinate();
            CoordinateDto thirdCoordinate = combinationDto.getThirdStore().getCoordinate();
            return calculateDistance(startCoordinate, firstCoordinate)
                    + calculateDistance(firstCoordinate, secondCoordinate)
                    + calculateDistance(secondCoordinate, thirdCoordinate);
        }
        if (totalRounds == 2) {
            CoordinateDto firstCoordinate = combinationDto.getFirstStore().getCoordinate();
            CoordinateDto secondCoordinate = combinationDto.getSecondStore().getCoordinate();
            return calculateDistance(startCoordinate, firstCoordinate)
                    + calculateDistance(firstCoordinate, secondCoordinate);
        }
        if (totalRounds == 1) {
            CoordinateDto firstCoordinate = combinationDto.getFirstStore().getCoordinate();
            return calculateDistance(startCoordinate, firstCoordinate);
        }
        throw new ApiException(INVALID_GATHERING_ROUND);
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
