package com.team22;

/**
 * Concrete class representing a satellite object.
 * Inherits all shared fields from SpaceObject.
 */

public class Satellite extends SpaceObject {
    public Satellite(String recordId, String satelliteName, String country, String approximateOrbitType,
            String objectType, int launchYear, String launchSite, double longitude,
            double avgLongitude, String geohash, int daysOld, int conjunctionCount) {
        super(recordId, satelliteName, country, approximateOrbitType, objectType, launchYear,
                launchSite, longitude, avgLongitude, geohash, daysOld, conjunctionCount);
    }

    @Override
    public String getObjectType() {
        return "Satellite";
    }
}