package com.github.szsascha.strangermapbackend.util;

public class Location {

    public static Double distanceBetweenTwoLocationsInKm(Double latitudeOne, Double longitudeOne, Double latitudeTwo, Double longitudeTwo) {
        if (latitudeOne == null || latitudeTwo == null || longitudeOne == null || longitudeTwo == null) {
            return null;
        }

        Double earthRadius = 6371.0;
        double diffBetweenLatitudeRadians = Math.toRadians(latitudeTwo - latitudeOne);
        double diffBetweenLongitudeRadians = Math.toRadians(longitudeTwo - longitudeOne);
        double latitudeOneInRadians = Math.toRadians(latitudeOne);
        double latitudeTwoInRadians = Math.toRadians(latitudeTwo);
        double a = Math.sin(diffBetweenLatitudeRadians / 2) * Math.sin(diffBetweenLatitudeRadians / 2) + Math.cos(latitudeOneInRadians) * Math.cos(latitudeTwoInRadians) * Math.sin(diffBetweenLongitudeRadians / 2)
                * Math.sin(diffBetweenLongitudeRadians / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (earthRadius * c);
    }

}