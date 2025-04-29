package com.team22;

import java.util.*;

/**
 * Provides analysis tools for long-term space debris impact and orbital density
 * evaluation.
 */

public class ImpactAnalysis {

    private List<SpaceObject> spaceObjects;

    public ImpactAnalysis(List<SpaceObject> spaceObjects) {
        this.spaceObjects = spaceObjects;
    }

    /**
     * Analyzes long-term impact by listing the top 10 oldest space objects and
     * their drift.
     */

    public void analyzeLongTermImpact() {
        spaceObjects.stream()
                .sorted(Comparator.comparingInt(o -> -o.daysOld))
                .limit(10)
                .forEach(obj -> {
                    System.out.println("RecordID: " + obj.recordId + ", Name: " + obj.satelliteName +
                            ", Days Old: " + obj.daysOld + ", Drift: " + obj.getOrbitalDrift());
                });

        Logger.log("Agency analyzed long-term impact: listed 10 oldest objects with drift.");
    }

    /**
     * Generates a density report based on the orbit type classification.
     * Prints object counts grouped by orbit region (LEO, MEO, HEO, UNKNOWN).
     */

    public void generateDensityReport() {
        Map<String, Integer> orbitCounts = new HashMap<>();

        for (SpaceObject obj : spaceObjects) {
            String type = obj.approximateOrbitType != null ? obj.approximateOrbitType.toUpperCase() : "UNKNOWN";
            orbitCounts.put(type, orbitCounts.getOrDefault(type, 0) + 1);
        }

        System.out.println("\n--- Debris Density Report by Orbit ---");
        for (Map.Entry<String, Integer> entry : orbitCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " objects");
        }

        Logger.log("Agency generated debris density report.");
    }
}
