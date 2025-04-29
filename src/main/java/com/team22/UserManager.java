package com.team22;

/**
 * Handles creation, listing, and deletion of user accounts.
 * Users are persisted in data/users.csv.
 */

import java.io.*;
import java.util.*;

public class UserManager {

    private static final String USER_FILE = "data/users.csv";

    /**
     * Prompts to create a new user and saves it.
     */

    public void createUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter role (Scientist, Admin, Agency, Policymaker): ");
        String role = scanner.nextLine().trim();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            writer.write(username + "," + role + "\n");
            System.out.println("User created successfully.");
            Logger.log("Admin created user: " + username + " with role: " + role);
        } catch (IOException e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    /**
     * Displays all users from the file.
     */

    public void listUsers() {
        File file = new File(USER_FILE);
        if (!file.exists()) {
            System.out.println("No users to display.");
            return;
        }

        System.out.println("\n--- User List ---");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            Logger.log("Admin viewed user list.");
        } catch (IOException e) {
            System.out.println("Error reading users: " + e.getMessage());
        }
    }

    /**
     * Deletes a user from the user file by name.
     */

    public void deleteUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username to delete: ");
        String target = scanner.nextLine().trim();

        File inputFile = new File(USER_FILE);
        File tempFile = new File("data/users_temp.csv");

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(target + ",")) {
                    writer.write(line + "\n");
                } else {
                    found = true;
                }
            }

            if (found) {
                System.out.println("User deleted: " + target);
                Logger.log("Admin deleted user: " + target);
                inputFile.delete();
                tempFile.renameTo(inputFile);
            } else {
                System.out.println("User not found.");
                tempFile.delete();
            }

        } catch (IOException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }
}
