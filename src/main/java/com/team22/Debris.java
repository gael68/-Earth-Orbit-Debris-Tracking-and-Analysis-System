/**
 * Concrete class representing a space debris object.
 * Inherits all shared fields from SpaceObject.
 */
package com.team22;

public class Debris extends SpaceObject {
    public Debris(String recordId, String satelliteName, String country, String approximateOrbitType,
            String objectType, int launchYear, String launchSite, double longitude,
            double avgLongitude, String geohash, int daysOld, int conjunctionCount) {
        super(recordId, satelliteName, country, approximateOrbitType, objectType, launchYear,
                launchSite, longitude, avgLongitude, geohash, daysOld, conjunctionCount);
    }

    @Override
    public String getObjectType() {
        return "Debris";
    }
}