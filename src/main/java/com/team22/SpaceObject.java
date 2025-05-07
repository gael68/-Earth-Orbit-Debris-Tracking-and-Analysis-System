package com.team22;

/**
 * Abstract class representing a generic object in space.
 * This class serves as the base for more specific types like {@link Debris} or {@link Satellite}.
 * It encapsulates common attributes such as orbit information, launch data, and tracking metadata.
 */
public abstract class SpaceObject {

    /** Unique record identifier for the space object. */
    protected String recordId;

    /** Name of the satellite or object. */
    protected String satelliteName;

    /** Country associated with the object. */
    protected String country;

    /** Approximate orbit type (e.g., LEO, MEO, GEO). */
    protected String approximateOrbitType;

    /** Type of object (e.g., DEBRIS, SATELLITE, ROCKET BODY). */
    protected String objectType;

    /** Year the object was launched. */
    protected int launchYear;

    /** Name of the launch site. */
    protected String launchSite;

    /** Current longitude position. */
    protected double longitude;

    /** Average longitude over time. */
    protected double avgLongitude;

    /** Geohash location string for the object. */
    protected String geohash;

    /** Number of days the object has been in orbit. */
    protected int daysOld;

    /** Number of conjunction (near-collision) events detected for the object. */
    protected int conjunctionCount;

    /**
     * Constructs a SpaceObject with all required tracking and metadata fields.
     *
     * @param recordId           unique record ID
     * @param satelliteName      satellite name
     * @param country            country of origin
     * @param approximateOrbitType  orbit type classification
     * @param objectType         type of object
     * @param launchYear         year of launch
     * @param launchSite         launch location
     * @param longitude          current longitude
     * @param avgLongitude       average longitude
     * @param geohash            geohash of object location
     * @param daysOld            age in days
     * @param conjunctionCount   number of conjunctions
     */
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

    /**
     * Gets the type of the object (e.g., "Satellite" or "Debris").
     *
     * @return the object type as a string
     */
    public abstract String getObjectType();

    /**
     * Calculates the orbital drift (difference between current and average longitude).
     *
     * @return the absolute value of the drift
     */
    public double getOrbitalDrift() {
        return Math.abs(longitude - avgLongitude);
    }

    /**
     * Provides a basic string representation of the object’s key attributes.
     *
     * @return comma-separated string of object metadata
     */
    public String getBasicInfo() {
        return recordId + ", " + satelliteName + ", " + country + ", " + approximateOrbitType + ", " +
                launchYear + ", " + launchSite + ", " + longitude + ", " + avgLongitude + ", " +
                geohash + ", " + daysOld;
    }
}
