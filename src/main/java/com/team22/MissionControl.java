package com.team22;

import java.io.*;
import java.util.*;

/**
 * Central controller class that loads data, manages menus,
 * and routes user input to the appropriate functionality.
 */
public class MissionControl {
    private List<SpaceObject> spaceObjects;
    private final String dataFilePath;
    private final Scanner scanner;

    public MissionControl() {
        this("data/space_objects.csv", new Scanner(System.in));
    }

    public MissionControl(String dataFilePath) {
        this(dataFilePath, new Scanner(System.in));
    }

    public MissionControl(String dataFilePath, Scanner scanner) {
        ensureDefaultUsersFile();
        this.dataFilePath = dataFilePath;
        this.spaceObjects = new ArrayList<>();
        this.scanner = scanner;
    }

    private void ensureDefaultUsersFile() {
        File userFile = new File("data/users.csv");
        if (!userFile.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile))) {
                writer.write("alice,Scientist,pass123\n");
                writer.write("bob,Admin,adminpass\n");
                writer.write("carol,Agency,agencypass\n");
                writer.write("dave,Policymaker,policy123\n");
                System.out.println("Default users file created.");
                Logger.log("Default users.csv created with default roles.");
            } catch (IOException e) {
                System.out.println("Failed to create default users file: " + e.getMessage());
            }
        }
    }

    public void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFilePath))) {
            String header = br.readLine();
            if (header == null) {
                System.out.println("CSV file is empty.");
                return;
            }

            String headerLine = header.replaceAll("^(ï»¿|\\uFEFF)", "").trim();
            System.out.println(headerLine);

            String[] headers = headerLine.split(",", -1);
            Map<String, Integer> colIndex = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                colIndex.put(headers[i].trim().toLowerCase(), i);
            }

            // List of required columns
            String[] requiredColumns = {
                    "record_id", "satellite_name", "country", "approximate_orbit_type", "object_type",
                    "launch_year", "launch_site", "longitude", "avg_longitude", "geohash",
                    "days_old", "conjunction_count"
            };

            // Check for missing columns
            List<String> missingColumns = new ArrayList<>();
            for (String column : requiredColumns) {
                if (!colIndex.containsKey(column)) {
                    missingColumns.add(column);
                }
            }

            if (!missingColumns.isEmpty()) {
                System.out.println("Missing required columns in CSV header: " + String.join(", ", missingColumns));
                return;
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < headers.length)
                    continue;

                String recordId = parts[colIndex.get("record_id")].trim();
                String satelliteName = parts[colIndex.get("satellite_name")].trim();
                String country = parts[colIndex.get("country")].trim();
                String approximateOrbitType = parts[colIndex.get("approximate_orbit_type")].trim();
                String objectType = parts[colIndex.get("object_type")].trim();
                int launchYear = parseIntSafe(parts[colIndex.get("launch_year")]);
                String launchSite = parts[colIndex.get("launch_site")].trim();
                double longitude = parseDoubleSafe(parts[colIndex.get("longitude")]);
                double avgLongitude = parseDoubleSafe(parts[colIndex.get("avg_longitude")]);
                String geohash = parts[colIndex.get("geohash")].trim();
                int daysOld = parseIntSafe(parts[colIndex.get("days_old")]);
                int conjunctionCount = parseIntSafe(parts[colIndex.get("conjunction_count")]);

                SpaceObject obj = objectType.equalsIgnoreCase("debris")
                        ? new Debris(recordId, satelliteName, country, approximateOrbitType, objectType,
                                launchYear, launchSite, longitude, avgLongitude, geohash,
                                daysOld, conjunctionCount)
                        : new Satellite(recordId, satelliteName, country, approximateOrbitType, objectType,
                                launchYear, launchSite, longitude, avgLongitude, geohash,
                                daysOld, conjunctionCount);

                spaceObjects.add(obj);
            }

            System.out.println("Data loaded from " + dataFilePath);
        } catch (IOException e) {
            System.out.println("Failed to read data: " + e.getMessage());
        }
    }

    public void runSimulation() {
        while (true) {
            System.out.println("\n=== User Type Menu ===");
            System.out.println("1. Scientist\n2. Space Agency Rep\n3. Policymaker\n4. Administrator\n5. Exit");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> authorizeUser("Scientist");
                case "2" -> authorizeUser("Agency");
                case "3" -> authorizeUser("Policymaker");
                case "4" -> authorizeUser("Admin");
                case "5", "EXIT" -> {
                    System.out.println("Saving and exiting...");
                    return;
                }
                default -> System.out.println("Invalid selection.");
            }
        }
    }

    public void authorizeUser(String userType) {
        UserManager userManager = new UserManager();
        if (!userManager.verifyLogin(userType, scanner)) {
            System.out.println("Access denied.");
            return;
        }

        switch (userType) {
            case "Scientist" -> scientistMenu();
            case "Agency" -> agencyMenu();
            case "Policymaker" -> policymakerMenu();
            case "Admin" -> adminMenu();
        }
    }

    private void scientistMenu() {
        while (true) {
            System.out.println("\n--- Scientist Menu ---");
            System.out.println("1. Track Objects in Space\n2. Assess Orbit Status\n3. Back");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> new TrackingSystem(spaceObjects).displayTrackingMenu();
                case "2" -> new OrbitAssessment(spaceObjects).assessAndSave();
                case "3" -> {
                    return;
                }
                default -> System.out.println("Invalid input.");
            }
        }
    }

    private void agencyMenu() {
        Scanner scnr = new Scanner(System.in);
        ReportContext context = new ReportContext(new ImpactReportStrategy());

        while (true) {
            System.out.println("\n--- Space Agency Representative Menu ---");
            System.out.println("1. Analyze Long-term Impact");
            System.out.println("2. Generate Density Reports");
            System.out.println("3. Full Report");
            System.out.println("4. Back");
            System.out.print("Choice: ");
            String choice = scnr.nextLine();

            switch (choice) {
                case "1" -> new ImpactAnalysis(spaceObjects).analyzeLongTermImpact();
                case "2" -> new ImpactAnalysis(spaceObjects).generateDensityReport();
                case "3" -> context.runReport(spaceObjects); // full report via strategy
                case "4" -> {
                    return;
                }
                default -> System.out.println("Invalid input.");
            }
        }
    }

    private void policymakerMenu() {
        PolicymakerAnalysis analysis = new PolicymakerAnalysis();
        while (true) {
            System.out.println("\n--- Policymaker Menu ---");
            System.out.println("1. Review Reports on Debris Impact");
            System.out.println("2. Assess Risk Levels for Future Space Missions");
            System.out.println("3. Back");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> analysis.reviewImpactReport();
                case "2" -> analysis.assessRiskLevels();
                case "3" -> {
                    return;
                }
                default -> System.out.println("Invalid input.");
            }
        }
    }

    private void adminMenu() {
        UserManager userManager = new UserManager();
        while (true) {
            System.out.println("\n--- Administrator Menu ---");
            System.out.println("1. Create User");
            System.out.println("2. View Users");
            System.out.println("3. Manage Users");
            System.out.println("4. Delete User");
            System.out.println("5. Back");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> userManager.createUser();
                case "2" -> userManager.listUsers();
                case "3" -> userManager.manageUser();
                case "4" -> userManager.deleteUser();
                case "5" -> {
                    return;
                }
                default -> System.out.println("Invalid input.");
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

    public List<SpaceObject> getSpaceObjects() {
        return spaceObjects;
    }
}