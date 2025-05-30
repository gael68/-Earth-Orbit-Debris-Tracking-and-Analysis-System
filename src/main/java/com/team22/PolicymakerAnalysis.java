package com.team22;

import java.io.*;

/**
 * Provides analysis tools for policymakers to review impact reports and assess
 * risk levels.
 */
public class PolicymakerAnalysis {

    /**
     * Displays the contents of the exited debris TXT report if it exists.
     */
    public void reviewImpactReport() {
        File file = new File("data/exited_debris_report.txt");
        if (!file.exists()) {
            System.out.println("No impact report available. Please run orbit assessment first.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            System.out.println("\n--- Exited Debris Impact Report ---");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            Logger.log("Policymaker reviewed the exited debris report.");
        } catch (IOException e) {
            System.out.println("Error reading report: " + e.getMessage());
        }
    }

    /**
     * Loads and analyzes the risk level summary from updated_orbit_status.csv.
     * Groups the debris by High, Moderate, and Low risk levels.
     */
    public void assessRiskLevels() {
        File file = new File("data/updated_orbit_status.csv");
        if (!file.exists()) {
            System.out.println("No orbit status data available. Please run orbit assessment first.");
            return;
        }

        int high = 0, moderate = 0, low = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length <= 13)
                    continue; // skip if risk column missing

                String risk = parts[13].trim().toLowerCase();
                switch (risk) {
                    case "high" -> high++;
                    case "moderate" -> moderate++;
                    case "low" -> low++;
                    default -> {
                    } // ignore unrecognized values
                }
            }

            System.out.println("\n--- Risk Level Summary ---");
            System.out.println("High Risk Objects: " + high);
            System.out.println("Moderate Risk Objects: " + moderate);
            System.out.println("Low Risk Objects: " + low);

            Logger.log("Policymaker assessed risk levels: High=" + high + ", Moderate=" + moderate + ", Low=" + low);
        } catch (IOException e) {
            System.out.println("Error reading risk data: " + e.getMessage());
        }
    }
}
