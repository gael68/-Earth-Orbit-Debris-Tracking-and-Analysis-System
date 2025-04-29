package com.team22;

/**
 * Central controller class that loads data, manages menus,
 * and routes user input to the appropriate functionality.
 */

import java.util.*;
import java.io.*;

public class MissionControl {
    private List<SpaceObject> spaceObjects;

    public MissionControl() {
        spaceObjects = new ArrayList<>();
    }

    /**
     * Reads the debris/satellite file and parses each row into SpaceObject
     * instances.
     * 
     * @param filename path to the input file
     */

    public void loadData(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 20)
                    continue;

                String recordId = parts[0].trim();
                String satelliteName = parts[2].trim();
                String country = parts[3].trim();
                String approximateOrbitType = parts[4].trim();
                String objectType = parts[5].trim();
                int launchYear = parseIntSafe(parts[6]);
                String launchSite = parts[7].trim();
                double longitude = parseDoubleSafe(parts[8]);
                double avgLongitude = parseDoubleSafe(parts[9]);
                String geohash = parts[10].trim();
                int daysOld = parseIntSafe(parts[18]);
                int conjunctionCount = parseIntSafe(parts[19]);

                SpaceObject obj;
                if (objectType.equalsIgnoreCase("DEBRIS")) {
                    obj = new Debris(recordId, satelliteName, country, approximateOrbitType, objectType,
                            launchYear, launchSite, longitude, avgLongitude, geohash,
                            daysOld, conjunctionCount);
                } else {
                    obj = new Satellite(recordId, satelliteName, country, approximateOrbitType, objectType,
                            launchYear, launchSite, longitude, avgLongitude, geohash,
                            daysOld, conjunctionCount);
                }

                spaceObjects.add(obj);
            }
            System.out.println("Data loaded from " + filename);
        } catch (IOException e) {
            System.out.println("Failed to read data: " + e.getMessage());
        }
    }

    public void runSimulation() {
        Scanner scnr = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== User Type Menu ===");
            System.out.println("1. Scientist\n2. Space Agency Rep\n3. Policymaker\n4. Administrator\n5. Exit");
            System.out.print("Enter choice: ");
            String choice = scnr.nextLine();

            switch (choice) {
                case "1":
                    authorizeUser("Scientist");
                    break;
                case "2":
                    authorizeUser("Agency");
                    break;
                case "3":
                    authorizeUser("Policymaker");
                    break;
                case "4":
                    authorizeUser("Admin");
                    break;
                case "5":
                case "EXIT":
                    System.out.println("Saving and exiting...");
                    return;
                default:
                    System.out.println("Invalid selection.");
            }
        }
    }

    public void authorizeUser(String userType) {
        switch (userType) {
            case "Scientist":
                scientistMenu();
                break;
            case "Agency":
                agencyMenu();
                break;
            case "Policymaker":
                policymakerMenu();
                break;
            case "Admin":
                adminMenu();
                break;
        }
    }

    private void scientistMenu() {
        Scanner scnr = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Scientist Menu ---");
            System.out.println("1. Track Objects in Space\n2. Assess Orbit Status\n3. Back");
            System.out.print("Choice: ");
            String choice = scnr.nextLine();

            switch (choice) {
                case "1":
                    TrackingSystem tracker = new TrackingSystem(spaceObjects);
                    tracker.displayTrackingMenu();
                    break;
                case "2":
                    OrbitAssessment oa = new OrbitAssessment(spaceObjects);
                    oa.assessAndSave();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid input.");
            }
        }
    }

    /**
     * Displays the menu for Space Agency Representatives.
     * Allows analyzing long-term impact and generating density reports
     * using the ImpactAnalysis module.
     */
    private void agencyMenu() {
        Scanner scnr = new Scanner(System.in);
        ImpactAnalysis analysis = new ImpactAnalysis(spaceObjects);

        while (true) {
            System.out.println("\n--- Space Agency Representative Menu ---");
            System.out.println("1. Analyze Long-term Impact");
            System.out.println("2. Generate Density Reports");
            System.out.println("3. Back");
            System.out.print("Choice: ");
            String choice = scnr.nextLine();

            switch (choice) {
                case "1":
                    analysis.analyzeLongTermImpact();
                    break;
                case "2":
                    analysis.generateDensityReport();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid input.");
            }
        }
    }

    /**
     * Displays the menu for Policymakers.
     * Enables reviewing impact reports and assessing risk levels from CSV data.
     */

    private void policymakerMenu() {
        Scanner scnr = new Scanner(System.in);
        PolicymakerAnalysis analysis = new PolicymakerAnalysis();

        while (true) {
            System.out.println("\n--- Policymaker Menu ---");
            System.out.println("1. Review Reports on Debris Impact");
            System.out.println("2. Assess Risk Levels for Future Space Missions");
            System.out.println("3. Back");
            System.out.print("Choice: ");
            String choice = scnr.nextLine();

            switch (choice) {
                case "1":
                    analysis.reviewImpactReport();
                    break;
                case "2":
                    analysis.assessRiskLevels();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid input.");
            }
        }
    }

    /**
     * Displays the Administrator menu.
     * Enables user creation, listing, and deletion using the UserManager.
     */

    private void adminMenu() {
        Scanner scanner = new Scanner(System.in);
        UserManager userManager = new UserManager();

        while (true) {
            System.out.println("\n--- Administrator Menu ---");
            System.out.println("1. Create User");
            System.out.println("2. View Users");
            System.out.println("3. Delete User");
            System.out.println("4. Back");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    userManager.createUser();
                    break;
                case "2":
                    userManager.listUsers();
                    break;
                case "3":
                    userManager.deleteUser();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid input.");
            }
        }
    }

    private int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * Provides access to the loaded space objects for other system modules.
     * 
     * @return list of parsed space objects
     */

    public List<SpaceObject> getSpaceObjects() {
        return spaceObjects;
    }
}