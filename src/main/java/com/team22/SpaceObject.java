package com.team22;

/**
 * Abstract class representing a generic object in space.
 * All specific space objects (e.g., debris or satellites) inherit from this.
 */

public abstract class SpaceObject {
    protected String recordId;
    protected String satelliteName;
    protected String country;
    protected String approximateOrbitType;
    protected String objectType;
    protected int launchYear;
    protected String launchSite;
    protected double longitude;
    protected double avgLongitude;
    protected String geohash;
    protected int daysOld;
    protected int conjunctionCount;

    public SpaceObject(String recordId, String satelliteName, String country, String approximateOrbitType,
            String objectType, int launchYear, String launchSite, double longitude,
            double avgLongitude, String geohash, int daysOld, int conjunctionCount) {
        this.recordId = recordId;
        this.satelliteName = satelliteName;
        this.country = country;
        this.approximateOrbitType = approximateOrbitType;
        this.objectType = objectType;
        this.launchYear = launchYear;
        this.launchSite = launchSite;
        this.longitude = longitude;
        this.avgLongitude = avgLongitude;
        this.geohash = geohash;
        this.daysOld = daysOld;
        this.conjunctionCount = conjunctionCount;
    }

    public abstract String getObjectType();

    public double getOrbitalDrift() {
        return Math.abs(longitude - avgLongitude);
    }

    public String getBasicInfo() {
        return recordId + ", " + satelliteName + ", " + country + ", " + approximateOrbitType + ", " +
                launchYear + ", " + launchSite + ", " + longitude + ", " + avgLongitude + ", " +
                geohash + ", " + daysOld;
    }
}