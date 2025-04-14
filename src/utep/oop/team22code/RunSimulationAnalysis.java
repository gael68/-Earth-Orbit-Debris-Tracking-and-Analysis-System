package utep.oop.team22code;

import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class RunSimulationAnalysis {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the Earth Orbit Debris Tracking Analysis Tool");

        String userType = determineUserType();

        if (userType.equals("Exit")) {
            System.out.println("Exiting the system. Goodbye!");
        } else {
            System.out.println("Initializing system for " + userType + " access...");
        }

    }

    /**
     * Determines the type of user interacting with the system using a Map.
     * Prompts the user to select their role from a list of options.
     * 
     * @return A String representing the user type selected
     */
    public static String determineUserType() {
        Scanner scanner = new Scanner(System.in);
        String userType = "";
        boolean validSelection = false;

        // Create a dictionary (Map) of user types
        Map<String, String> userTypes = new HashMap<>();
        userTypes.put("1", "Scientist");
        userTypes.put("2", "Space Agency Representative");
        userTypes.put("3", "Policymaker");
        userTypes.put("4", "Administrator");
        userTypes.put("5", "Exit");

        while (!validSelection) {
            // Display user type options
            System.out.println("\nPlease select your user type:");
            System.out.println("1. Scientist");
            System.out.println("2. Space Agency Representative");
            System.out.println("3. Policymaker");
            System.out.println("4. Administrator");
            System.out.println("5. Exit");
            System.out.print("\nEnter your selection (1-5): ");

            // Get user input
            String input = scanner.nextLine().trim();

            // Process selection using the dictionary
            if (userTypes.containsKey(input)) {
                userType = userTypes.get(input);
                validSelection = true;
            } else {
                System.out.println("Invalid selection. Please enter a number between 1 and 5.");
            }
        }

        if (!userType.equals("Exit")) {
            System.out.println("You have selected: " + userType);
        }

        scanner.close();
        return userType;
    }
}
