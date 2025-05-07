package com.team22;

/**
 * Concrete class representing a satellite object in space.
 * This class extends {@link SpaceObject} and provides a specific object type identifier.
 */
public class Satellite extends SpaceObject {

    /**
     * Constructs a Satellite object with all required metadata.
     *
     * @param recordId         unique record ID
     * @param satelliteName    name of the satellite
     * @param country          country of origin
     * @param approximateOrbitType orbit type (e.g., LEO, MEO, GEO)
     * @param objectType       type of object (typically "SATELLITE")
     * @param launchYear       year the object was launched
     * @param launchSite       location of launch
     * @param longitude        current longitude
     * @param avgLongitude     average longitude
     * @param geohash          geohash of the object
     * @param daysOld          number of days in orbit
     * @param conjunctionCount number of conjunction events
     */
    public Satellite(String recordId, String satelliteName, String country, String approximateOrbitType,
                     String objectType, int launchYear, String launchSite, double longitude,
                     double avgLongitude, String geohash, int daysOld, int conjunctionCount) {
        super(recordId, satelliteName, country, approximateOrbitType, objectType, launchYear,
              launchSite, longitude, avgLongitude, geohash, daysOld, conjunctionCount);
    }

    /**
     * Returns the specific object type represented by this class.
     *
     * @return the string "Satellite"
     */
    @Override
    public String getObjectType() {
        return "Satellite";
    }
}
