package com.team22;

/**
 * Handles the tracking of space objects by object type category
 * (e.g., Debris, Rocket Body, Payload, Unknown).
 */

import java.util.List;
import java.util.Scanner;

public class TrackingSystem {

    private List<SpaceObject> spaceObjects;

    /**
     * Constructs a tracking system with the provided list of space objects.
     * 
     * @param spaceObjects list of loaded space debris and satellite objects
     */

    public TrackingSystem(List<SpaceObject> spaceObjects) {
        this.spaceObjects = spaceObjects;
    }

    /**
     * Displays the tracking menu for Scientists to choose a category of objects to
     * list.
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
                case "1":
                    trackByType("ROCKET BODY");
                    break;
                case "2":
                    trackByType("DEBRIS");
                    break;
                case "3":
                    trackByType("PAYLOAD");
                    break;
                case "4":
                    trackByType("UNKNOWN");
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid input.");
            }
        }
    }

    /**
     * Filters and prints objects that match the specified object type.
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
