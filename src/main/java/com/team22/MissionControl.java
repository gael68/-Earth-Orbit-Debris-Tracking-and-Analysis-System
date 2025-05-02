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
        this.dataFilePath = dataFilePath;
        this.spaceObjects = new ArrayList<>();
        this.scanner = scanner;
    }

    public void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFilePath))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 20)
                    continue;

                SpaceObject obj;
                String objectType = parts[5].trim();
                if (objectType.equalsIgnoreCase("DEBRIS")) {
                    obj = new Debris(parts[0].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim(), objectType,
                            parseIntSafe(parts[6]), parts[7].trim(), parseDoubleSafe(parts[8]),
                            parseDoubleSafe(parts[9]),
                            parts[10].trim(), parseIntSafe(parts[18]), parseIntSafe(parts[19]));
                } else {
                    obj = new Satellite(parts[0].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim(), objectType,
                            parseIntSafe(parts[6]), parts[7].trim(), parseDoubleSafe(parts[8]),
                            parseDoubleSafe(parts[9]),
                            parts[10].trim(), parseIntSafe(parts[18]), parseIntSafe(parts[19]));
                }

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
        ImpactAnalysis analysis = new ImpactAnalysis(spaceObjects);
        while (true) {
            System.out.println("\n--- Space Agency Representative Menu ---");
            System.out.println("1. Analyze Long-term Impact");
            System.out.println("2. Generate Density Reports");
            System.out.println("3. Back");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> analysis.analyzeLongTermImpact();
                case "2" -> analysis.generateDensityReport();
                case "3" -> {
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
            System.out.println("3. Delete User");
            System.out.println("4. Back");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> userManager.createUser();
                case "2" -> userManager.listUsers();
                case "3" -> userManager.deleteUser();
                case "4" -> {
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