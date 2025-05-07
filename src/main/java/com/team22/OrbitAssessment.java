package com.team22;

/**
 * Evaluates whether space objects are still in orbit based on criteria
 * such as orbit type, days old, conjunction count, and longitude.
 * Also classifies risk levels and exports detailed reports.
 */
import java.io.*;
import java.util.*;

public class OrbitAssessment {

    private List<SpaceObject> spaceObjects;
    private final List<SpaceObject> exitedObjects = new ArrayList<>();
    private int inOrbitCount = 0;
    private int exitedCount = 0;

    /**
     * Constructs an OrbitAssessment engine for a provided list of objects.
     * 
     * @param spaceObjects list of loaded space objects
     */
    public OrbitAssessment(List<SpaceObject> spaceObjects) {
        this.spaceObjects = spaceObjects;
    }

    /**
     * Runs the orbit status assessment, classifies risk levels,
     * and writes updated reports to CSV and TXT.
     */
    public void assessAndSave() {
        for (SpaceObject obj : spaceObjects) {
            boolean stillInOrbit = isStillInOrbit(obj);
            String riskLevel = classifyRisk(obj.getOrbitalDrift());

            if (!stillInOrbit) {
                exitedObjects.add(obj);
                exitedCount++;
            } else {
                inOrbitCount++;
            }
        }

        saveCSV("data/updated_orbit_status.csv");
        saveExitedTXT("data/exited_debris_report.txt");
        Logger.log("Scientist assessed orbit status. In-orbit: " + inOrbitCount + ", Exited: " + exitedCount);
        System.out.println("Assessment complete. Output files saved.");
    }

    /**
     * Determines if a space object is still in orbit.
     * 
     * @param obj space object
     * @return true if still in orbit, false if exited
     */
    private boolean isStillInOrbit(SpaceObject obj) {
        boolean hasValidOrbit = obj.approximateOrbitType != null
                && !obj.approximateOrbitType.equalsIgnoreCase("unknown");
        boolean hasValidLongitude = obj.longitude != 0;
        boolean recent = obj.daysOld < 15000;
        boolean hasConjunctions = obj.conjunctionCount >= 1;

        return hasValidOrbit && hasValidLongitude && recent && hasConjunctions;
    }

    /**
     * Classifies the orbital drift into a risk level.
     * 
     * @param drift calculated orbital drift
     * @return risk level: High, Moderate, or Low
     */
    private String classifyRisk(double drift) {
        if (drift > 50)
            return "High";
        if (drift > 10)
            return "Moderate";
        return "Low";
    }

    /**
     * Writes a CSV file with updated orbit and risk status for each object.
     * 
     * @param filename name of output CSV file
     */
    private void saveCSV(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(
                    "record_id,satellite_name,country,approximate_orbit_type,object_type,launch_year,launch_site,longitude,avg_longitude,geohash,days_old,conjunction_count,still_in_orbit,risk_level\n");

            for (SpaceObject obj : spaceObjects) {
                boolean still = isStillInOrbit(obj);
                String risk = classifyRisk(obj.getOrbitalDrift());

                writer.write(obj.recordId + "," + obj.satelliteName + "," + obj.country + "," +
                        obj.approximateOrbitType + "," + obj.objectType + "," + obj.launchYear + "," + obj.launchSite
                        + "," +
                        obj.longitude + "," + obj.avgLongitude + "," + obj.geohash + "," + obj.daysOld + "," +
                        obj.conjunctionCount + "," + still + "," + risk + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing updated CSV: " + e.getMessage());
        }
    }

    /**
     * Writes a TXT file containing only exited debris and a summary count.
     * 
     * @param filename name of output TXT file
     */
    private void saveExitedTXT(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Exited Debris Report\n");
            writer.write("Total in orbit: " + inOrbitCount + "\n");
            writer.write("Total exited: " + exitedCount + "\n\n");

            for (SpaceObject obj : exitedObjects) {
                writer.write("RecordID: " + obj.recordId + ", Name: " + obj.satelliteName +
                        ", Country: " + obj.country + ", OrbitType: " + obj.approximateOrbitType +
                        ", LaunchYear: " + obj.launchYear + ", LaunchSite: " + obj.launchSite +
                        ", Longitude: " + obj.longitude + ", AvgLongitude: " + obj.avgLongitude +
                        ", Geohash: " + obj.geohash + ", DaysOld: " + obj.daysOld + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing exited debris TXT: " + e.getMessage());
        }
    }
}
