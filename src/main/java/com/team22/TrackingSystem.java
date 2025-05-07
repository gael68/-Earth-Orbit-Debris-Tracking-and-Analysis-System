package com.team22;

import java.util.List;
import java.util.Scanner;

/**
 * Handles the tracking of space objects by object type category
 * (e.g., Debris, Rocket Body, Payload, Unknown).
 * Used by Scientists to filter and view specific classes of objects in orbit.
 */
public class TrackingSystem {

    private List<SpaceObject> spaceObjects;

    /**
     * Constructs a TrackingSystem instance using a list of space objects.
     *
     * @param spaceObjects list of loaded space debris and satellite objects
     */
    public TrackingSystem(List<SpaceObject> spaceObjects) {
        this.spaceObjects = spaceObjects;
    }

    /**
     * Displays a menu interface for scientists to select and track
     * objects by category.
     * 
     * Categories include:
     * - Rocket Body
     * - Debris
     * - Payload
     * - Unknown
     * 
     * Loops until the user selects "Back".
     */
    public void displayTrackingMenu() {
        Scanner scanner = new Scanner(System.in);
        String choice;

        while (true) {
            System.out.println("\n--- Track Objects Menu ---");
            System.out.println("1. Rocket Body");
            System.out.println("2. Debris");
            System.out.println("3. Payload");
            System.out.println("4. Unknown");
            System.out.println("5. Back");

            System.out.print("Select an option: ");
            choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> trackByType("ROCKET BODY");
                case "2" -> trackByType("DEBRIS");
                case "3" -> trackByType("PAYLOAD");
                case "4" -> trackByType("UNKNOWN");
                case "5" -> {
                    return;
                }
                default -> System.out.println("Invalid input.");
            }
        }
    }

    /**
     * Filters and displays all space objects that match a given type.
     *
     * @param type the object type to track (e.g., "DEBRIS")
     */
    private void trackByType(String type) {
        boolean found = false;
        System.out.println("\n--- Tracking: " + type + " ---");

        for (SpaceObject obj : spaceObjects) {
            if (obj.objectType.equalsIgnoreCase(type)) {
                System.out.println(obj.getBasicInfo());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No objects found for type: " + type);
        }

        Logger.log("Scientist viewed objects in category: " + type);
    }
}
