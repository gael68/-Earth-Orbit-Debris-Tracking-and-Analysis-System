package com.team22;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides analysis tools for long-term space debris impact and orbital density
 * evaluation. This class supports two primary analyses:
 * <ul>
 *     <li>Long-term impact assessment based on LEO objects with significant age and conjunction activity.</li>
 *     <li>Density reporting based on user-defined longitude range filters.</li>
 * </ul>
 * 
 * Used by space agency representatives to make informed decisions based on
 * object behavior and risk profiles in Earth's orbit.
 */
public class ImpactAnalysis {

    private final List<SpaceObject> spaceObjects;

    /**
     * Constructs the ImpactAnalysis module with the provided list of space objects.
     *
     * @param spaceObjects the list of objects (debris or satellites) to analyze
     */
    public ImpactAnalysis(List<SpaceObject> spaceObjects) {
        this.spaceObjects = spaceObjects;
    }

    /**
     * Filters and displays LEO (Low Earth Orbit) objects that are older than 200 days
     * and have a conjunction count greater than zero. Prints a structured summary of
     * each qualifying object to the console.
     * 
     * This method helps agencies identify high-risk long-living LEO objects.
     */
    public void analyzeLongTermImpact() {
        System.out.println("\n--- Debug: Evaluating LEO objects ---");

        List<SpaceObject> filtered = new ArrayList<>();

        for (SpaceObject obj : spaceObjects) {
            boolean isLEO = "LEO".equalsIgnoreCase(obj.approximateOrbitType);
            boolean isOld = obj.daysOld > 200;
            boolean hasConj = obj.conjunctionCount > 0;

            if (isLEO && isOld && hasConj) {
                filtered.add(obj);
            }
        }

        if (filtered.isEmpty()) {
            System.out.println("No qualifying LEO objects found.");
            return;
        }

        System.out.println("\n--- Long-Term Impact Report (LEO > 200 days + conjunctions) ---");
        for (SpaceObject obj : filtered) {
            System.out.println("Record ID: " + obj.recordId +
                    ", Name: " + obj.satelliteName +
                    ", Country: " + obj.country +
                    ", Orbit Type: " + obj.approximateOrbitType +
                    ", Object Type: " + obj.objectType +
                    ", Days Old: " + obj.daysOld +
                    ", Conjunctions: " + obj.conjunctionCount);
        }

        Logger.log("Agency analyzed long-term impact for LEO objects.");
    }

    /**
     * Prompts the user to input a longitude range and filters the space objects
     * to only those within the specified range. Displays a count and detailed
     * information about each matching object.
     * 
     * This helps evaluate the concentration of space objects in specific orbital bands.
     */
    public void generateDensityReport() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter minimum longitude: ");
            double min = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter maximum longitude: ");
            double max = Double.parseDouble(scanner.nextLine().trim());

            List<SpaceObject> inRange = spaceObjects.stream()
                    .filter(obj -> obj.longitude >= min && obj.longitude <= max)
                    .collect(Collectors.toList());

            System.out.println("\n--- Debris Density Report ---");
            System.out.println("Total objects in range [" + min + ", " + max + "]: " + inRange.size());
            for (SpaceObject obj : inRange) {
                System.out.println("Record ID: " + obj.recordId +
                        ", Name: " + obj.satelliteName +
                        ", Country: " + obj.country +
                        ", Orbit Type: " + obj.approximateOrbitType +
                        ", Launch Year: " + obj.launchYear +
                        ", Object Type: " + obj.objectType);
            }

            Logger.log("Agency generated filtered density report.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid numbers.");
        }
    }
}
