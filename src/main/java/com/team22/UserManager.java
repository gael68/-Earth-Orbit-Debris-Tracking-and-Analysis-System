package com.team22;

import java.io.*;
import java.util.*;

/**
 * Handles creation, verification, listing, updating, and deletion of user accounts.
 * Users are stored in a configurable CSV file (default: data/users.csv).
 */
public class UserManager {

    private final String userFile;

    /**
     * Constructs a UserManager with the default file path.
     */
    public UserManager() {
        this("data/users.csv");
    }

    /**
     * Constructs a UserManager with a specified file path (useful for testing).
     *
     * @param userFile the path to the user CSV file
     */
    public UserManager(String userFile) {
        this.userFile = userFile;
    }

    /**
     * Verifies login credentials based on username, password, and user role.
     *
     * @param role    the role to verify (e.g., Scientist, Admin)
     * @param scanner a Scanner for reading user input
     * @return true if credentials are valid; false otherwise
     */
    public boolean verifyLogin(String role, Scanner scanner) {
        File file = new File("data/users.csv");
        if (!file.exists()) {
            System.out.println("User file missing.");
            return false;
        }

        System.out.print("Username: ");
        String inputUser = scanner.nextLine().trim();

        System.out.print("Password: ");
        String inputPass = scanner.nextLine().trim();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    String userRole = parts[1];
                    String password = parts[2];
                    if (username.equals(inputUser) && userRole.equalsIgnoreCase(role) && password.equals(inputPass)) {
                        Logger.log("Login successful for " + username + " (" + role + ")");
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Login error: " + e.getMessage());
        }

        System.out.println("Invalid credentials.");
        Logger.log("Failed login attempt for role: " + role);
        return false;
    }

    /**
     * Creates a new user by prompting for username and role,
     * then writing to the user file.
     */
    public void createUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter role (Scientist, Admin, Agency, Policymaker): ");
        String role = scanner.nextLine().trim();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile, true))) {
            writer.write(username + "," + role + "\n");
            System.out.println("User created successfully.");
            Logger.log("Admin created user: " + username + " with role: " + role);
        } catch (IOException e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    /**
     * Lists all users from the CSV file.
     */
    public void listUsers() {
        File file = new File(userFile);
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
     * Deletes a user by prompting for a username and removing the corresponding entry.
     */
    public void deleteUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username to delete: ");
        String target = scanner.nextLine().trim();

        File inputFile = new File(userFile);
        File tempFile = new File(userFile.replace(".csv", "_temp.csv"));

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

    /**
     * Updates an existing user's username and/or password.
     * Prompts for the target user and new values.
     */
    public void manageUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username to update: ");
        String target = scanner.nextLine().trim();

        File inputFile = new File(userFile);
        File tempFile = new File(userFile.replace(".csv", "_temp.csv"));

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(target)) {
                    found = true;
                    System.out.println("Found user: " + parts[0] + " (" + parts[1] + ")");

                    System.out.print("Enter new username (press Enter to keep current): ");
                    String newUsername = scanner.nextLine().trim();
                    if (newUsername.isEmpty())
                        newUsername = parts[0];

                    System.out.print("Enter new password (press Enter to keep current): ");
                    String newPassword = scanner.nextLine().trim();
                    if (newPassword.isEmpty())
                        newPassword = parts[2];

                    writer.write(newUsername + "," + parts[1] + "," + newPassword + "\n");
                    Logger.log("Admin updated user: " + parts[0]);
                } else {
                    writer.write(line + "\n");
                }
            }

            if (found) {
                inputFile.delete();
                tempFile.renameTo(inputFile);
                System.out.println("User updated successfully.");
            } else {
                tempFile.delete();
                System.out.println("User not found.");
            }

        } catch (IOException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }
}
