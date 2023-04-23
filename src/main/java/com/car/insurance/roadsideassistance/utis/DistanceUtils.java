package com.car.insurance.roadsideassistance.utis;

import com.car.insurance.roadsideassistance.domain.dto.Geolocation;
import com.car.insurance.roadsideassistance.domain.entity.GeolocationEntity;

/**
 * Utility class to measure distance based on standard distance calculations
 */
public class DistanceUtils {

    public static Double calculateDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(latitude2 - latitude1);
        double lonDistance = Math.toRadians(longitude2 - longitude1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }
}
