/**
 * Concrete class representing a space debris object.
 * Inherits all shared fields from SpaceObject and overrides the object type.
 * 
 * Used to model defunct objects in orbit that pose potential collision risks.
 */
package com.team22;

public class Debris extends SpaceObject {

    /**
     * Constructs a Debris object with all relevant orbital and identification information.
     *
     * @param recordId          unique identifier for the object
     * @param satelliteName     name of the object or associated satellite
     * @param country           country of origin or ownership
     * @param approximateOrbitType orbit classification (e.g., LEO, MEO)
     * @param objectType        the type of the object, should be "Debris"
     * @param launchYear        year the object was launched
     * @param launchSite        site from which the object was launched
     * @param longitude         current longitude of the object
     * @param avgLongitude      historical average longitude
     * @param geohash           spatial location encoding
     * @param daysOld           number of days since launch or tracking began
     * @param conjunctionCount  number of conjunction alerts associated with the object
     */
    public Debris(String recordId, String satelliteName, String country, String approximateOrbitType,
                  String objectType, int launchYear, String launchSite, double longitude,
                  double avgLongitude, String geohash, int daysOld, int conjunctionCount) {
        super(recordId, satelliteName, country, approximateOrbitType, objectType, launchYear,
              launchSite, longitude, avgLongitude, geohash, daysOld, conjunctionCount);
    }

    /**
     * Returns the specific object type for debris.
     *
     * @return the string "Debris"
     */
    @Override
    public String getObjectType() {
        return "Debris";
    }
}
