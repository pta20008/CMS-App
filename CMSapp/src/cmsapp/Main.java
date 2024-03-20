/*
 * https://github.com/pta20008/CMS-App.git
 */
package cmsapp;

/**
 *
 * @author bruno
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final String DB_URL = "jdbc:mysql://localhost/cms_db";
    private static final String DB_USER = "pooa2024";
    private static final String DB_PASSWORD = "pooa2024";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connection established successfully!");

            Scanner scanner = new Scanner(System.in);
            User authenticatedUser = authenticateUser(scanner);
            if (authenticatedUser == null) {
                System.out.println("Authentication failed. Exiting...");
                return;
            }

            displayMenu(authenticatedUser, scanner, connection);
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    private static User authenticateUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (username.equals("admin") && password.equals("java")) {
            return new User(username, UserRole.ADMIN);
        } else if (username.equals("office") && password.equals("office")) {
            return new User(username, UserRole.OFFICE);
        } else if (username.equals("lecturer") && password.equals("lecturer")) {
            return new User(username, UserRole.LECTURER);
        } else {
            System.out.println("Invalid username or password.");
            return null;
        }
    }

    private static void displayMenu(User user, Scanner scanner, Connection connection) {
        while (true) {
            System.out.println("\n===== College Management System Menu =====");
            System.out.println("User: " + user.getRole()); // Displays the role of the logged-in user
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
                    generateCourseReport(connection);
                    break;
                case 2:
                    generateStudentReport(connection);
                    break;
                case 3:
                    generateLecturerReport(connection);
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

    private static void generateCourseReport(Connection connection) {
        // Implement course report generation logic with database operations
        System.out.println("Generating Course Report...");
    }

    private static void generateStudentReport(Connection connection) {
        // Implement student report generation logic with database operations
        System.out.println("Generating Student Report...");
    }

    private static void generateLecturerReport(Connection connection) {
        // Implement lecturer report generation logic with database operations
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
