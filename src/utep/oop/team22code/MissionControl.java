package utep.oop.team22code;

import java.util.*;
import java.io.*;

public class MissionControl {

    private List<SpaceObject> spaceObjects;

    public MissionControl() {
        spaceObjects = new ArrayList<>();
    }

    public void loadData(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length < 11) continue;

                String recordId = parts[0].trim();
                String satelliteName = parts[1].trim();
                String country = parts[2].trim();
                String orbitType = parts[3].trim();
                int launchYear = Integer.parseInt(parts[4].trim());
                String launchSite = parts[5].trim();
                double longitude = parseDoubleSafe(parts[6]);
                double avgLongitude = parseDoubleSafe(parts[7]);
                String geohash = parts[8].trim();
                int daysOld = Integer.parseInt(parts[9].trim());
                int conjunctionCount = Integer.parseInt(parts[10].trim());

                SpaceObject obj;

                if (satelliteName.toLowerCase().contains("debris") || recordId.toLowerCase().contains("deb")) {
                    obj = new Debris(recordId, satelliteName, country, orbitType, launchYear,
                                     launchSite, longitude, avgLongitude, geohash,
                                     daysOld, conjunctionCount);
                } else {
                    obj = new Satellite(recordId, satelliteName, country, orbitType, launchYear,
                                        launchSite, longitude, avgLongitude, geohash,
                                        daysOld, conjunctionCount);
                }

                spaceObjects.add(obj);
            }

            System.out.println("Data successfully loaded from " + filename);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public void runSimulation() {
        Scanner scanner = new Scanner(System.in);
        String userType;

        while (true) {
            System.out.println("\n=== Select User Type ===");
            System.out.println("1. Scientist");
            System.out.println("2. Space Agency Representative");
            System.out.println("3. Policymaker");
            System.out.println("4. Administrator");
            System.out.println("5. Exit");

            System.out.print("Enter choice (1-5): ");
            userType = scanner.nextLine().trim();

            switch (userType) {
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
            default:
                System.out.println("User type not implemented yet.");
        }
    }

    private void scientistMenu() {
        Scanner scanner = new Scanner(System.in);
        String choice;

        while (true) {
            System.out.println("\n--- Scientist Menu ---");
            System.out.println("1. Track Objects in Space");
            System.out.println("2. Assess Orbit Status");
            System.out.println("3. Back");

            System.out.print("Enter choice: ");
            choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("Tracking menu coming soon...");
                    break;
                case "2":
                    System.out.println("Orbit assessment coming soon...");
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid input.");
            }
        }
    }

    private double parseDoubleSafe(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return 0.0;
        }
    }

    public List<SpaceObject> getSpaceObjects() {
        return spaceObjects;
    }
}
