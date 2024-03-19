/*
 * https://github.com/pta20008/CMS-App.git
 */
package cmsapp;

/**
 *
 * @author bruno
 */

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "java";
    private static final String OFFICE_USERNAME = "office";
    private static final String OFFICE_PASSWORD = "office";
    private static final String LECTURER_USERNAME = "lecturer";
    private static final String LECTURER_PASSWORD = "lecturer";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Authenticate the user
        User authenticatedUser = authenticateUser(scanner);
        if (authenticatedUser == null) {
            System.out.println("Authentication failed. Exiting...");
            return;
        }

        // Display menu based on user role
        displayMenu(authenticatedUser, scanner);
    }

    private static User authenticateUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            return new User(username, UserRole.ADMIN);
        } else if (username.equals(OFFICE_USERNAME) && password.equals(OFFICE_PASSWORD)) {
            return new User(username, UserRole.OFFICE);
        } else if (username.equals(LECTURER_USERNAME) && password.equals(LECTURER_PASSWORD)) {
            return new User(username, UserRole.LECTURER);
        } else {
            System.out.println("Invalid username or password.");
            return null;
        }
    }

    private static void displayMenu(User user, Scanner scanner) {
        while (true) {
            System.out.println("\n===== College Management System Menu =====");
            System.out.println("1. Generate Course Report");
            System.out.println("2. Generate Student Report");
            System.out.println("3. Generate Lecturer Report");

            if (user.getRole() == UserRole.OFFICE || user.getRole() == UserRole.ADMIN) {
                System.out.println("4. Change Password");
            }

            System.out.println("5. Logout");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    generateCourseReport();
                    break;
                case 2:
                    generateStudentReport();
                    break;
                case 3:
                    generateLecturerReport();
                    break;
                case 4:
                    if (user.getRole() == UserRole.OFFICE || user.getRole() == UserRole.ADMIN) {
                        changePassword(user, scanner);
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    private static void generateCourseReport() {
        // Implement course report generation logic
        System.out.println("Generating Course Report...");
    }

    private static void generateStudentReport() {
        // Implement student report generation logic
        System.out.println("Generating Student Report...");
    }

    private static void generateLecturerReport() {
        // Implement lecturer report generation logic
        System.out.println("Generating Lecturer Report...");
    }

    private static void changePassword(User user, Scanner scanner) {
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();

        if (!currentPassword.equals(user.getPassword())) {
            System.out.println("Incorrect password.");
            return;
        }

        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        user.setPassword(newPassword);
        System.out.println("Password changed successfully.");
    }
}

enum UserRole {
    ADMIN,
    OFFICE,
    LECTURER
}

class User {
    private String username;
    private String password;
    private UserRole role;

    public User(String username, UserRole role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public UserRole getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
