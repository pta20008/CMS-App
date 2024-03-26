/*
 * https://github.com/pta20008/CMS-App.git
 */
package cmsapp;

/**
 * Main class responsible for handling user authentication and menu display.
 * Built to manager user interactions and use appropriate methods based on user
 * input. Connects to the database and executes SQL queries.
 *
 * @author bruno
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import cmsapp.LecturerReportGenerator;
import cmsapp.OfficeReportGenerator;
import cmsapp.CourseReportGenerator;

public class Main {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost/cms_db";
    private static final String DB_USER = "pooa2024";
    private static final String DB_PASSWORD = "pooa2024";

    public static void main(String[] args) {
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connection established successfully!");
            System.out.println("Welcome, please log in!");
            // Initialize scanner for user input
            Scanner scanner = new Scanner(System.in);
            // Authenticate user
            User authenticatedUser = authenticateUser(scanner, connection);
            if (authenticatedUser == null) {
                System.out.println("Authentication failed. Exiting...");
                return;
            }
            // Display menu based on user role
            displayMenu(authenticatedUser, scanner, connection);
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    // Method to authenticate user by username and password
    private static User authenticateUser(Scanner scanner, Connection connection) {
        while (true) {
            try {
                System.out.print("\nEnter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                // SQL query to retrieve user information based on username and password
                String query = "SELECT username, role FROM users WHERE username = ? AND password = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, password);

                // Execute query and process results
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String retrievedUsername = resultSet.getString("username");
                    String roleString = resultSet.getString("role");
                    UserRole role = UserRole.valueOf(roleString.toUpperCase());
                    return new User(retrievedUsername, role);
                } else {
                    System.out.println("Invalid username or password. Please try again.");
                }
            } catch (SQLException e) {
                System.out.println("Error authenticating user: " + e.getMessage());
            }
        }
    }

    // Method to display menu options based on user role
    private static void displayMenu(User user, Scanner scanner, Connection connection) {
        while (true) {
            System.out.println("\n===== College Management System Menu =====");
            System.out.println("Welcome: " + user.getUsername() + "\nRole: (" + user.getRole() + ")"); // Show the username and the role when connected
            System.out.println();

            // Display menu options based on user role
            if (user.getRole() == UserRole.OFFICE) {
                System.out.println("1. Generate Course Report");
                System.out.println("2. Generate Student Report");
                System.out.println("3. Generate Lecturer Report");
                System.out.println("4. Change Username");
                System.out.println("5. Change Password");
                System.out.println("0. Logout");
            } else if (user.getRole() == UserRole.ADMIN) {
                System.out.println("4. Change Username");
                System.out.println("5. Change Password");
                System.out.println("6. Add User");
                System.out.println("7. Remove User");
                System.out.println("0. Logout");
            } else if (user.getRole() == UserRole.LECTURER) {
                System.out.println("3. Generate Lecturer Report");
                System.out.println("4. Change Username");
                System.out.println("5. Change Password");
                System.out.println("0. Logout");
            }

            // Prompt user for input and process choice
            System.out.print("\nEnter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    if (user.getRole() != UserRole.ADMIN && user.getRole() != UserRole.LECTURER) {
                        CourseReportGenerator.generateCourseReport(connection);
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;
                case 2:
                    if (user.getRole() != UserRole.ADMIN && user.getRole() != UserRole.LECTURER) {
                        OfficeReportGenerator.generateStudentReport(connection);
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;
                case 3:
                    if (user.getRole() != UserRole.ADMIN) {
                        if (user.getRole() == UserRole.LECTURER) {
                            LecturerReportGenerator.generateLecturerReport(user.getUsername(), connection);
                        }
                        if (user.getRole() == UserRole.OFFICE) {
                            LecturerReportGenerator.generateLecturerReport(user.getUsername(), connection);
                        } 
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;

                case 4:
                    changeUsername(user, scanner, connection);
                    break;
                case 5:
                    changePassword(user, scanner, connection);
                    break;
                case 0:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    // Method to change user password
    private static void changePassword(User user, Scanner scanner, Connection connection) {
        try {
            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine();

            String updateQuery = "UPDATE users SET password = ? WHERE username = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, newPassword);
            updateStatement.setString(2, user.getUsername());
            updateStatement.executeUpdate();

            System.out.println("Password changed successfully.");
        } catch (SQLException e) {
            System.out.println("Error changing password: " + e.getMessage());
        }
    }
    // Method to change username

    private static void changeUsername(User user, Scanner scanner, Connection connection) {
        try {
            System.out.print("Enter new username: ");
            String newUsername = scanner.nextLine();

            String updateQuery = "UPDATE users SET username = ? WHERE username = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, newUsername);
            updateStatement.setString(2, user.getUsername());
            updateStatement.executeUpdate();

            user.setUsername(newUsername); // Update the username in the User object as well
            System.out.println("Username changed successfully.");
        } catch (SQLException e) {
            System.out.println("Error changing username: " + e.getMessage());
        }
    }

    // Method to create a new user
    private static void addUser(Scanner scanner, Connection connection) {
        try {
            System.out.print("Enter username for the new user: ");
            String username = scanner.nextLine();
            System.out.print("Enter password for the new user: ");
            String password = scanner.nextLine();
            System.out.print("Enter role for the new user (ADMIN, OFFICE, LECTURER): ");
            String roleString = scanner.nextLine();

            // Convert role string to enum
            UserRole role = UserRole.valueOf(roleString.toUpperCase());

            // Insert new user into the database
            String insertQuery = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, username);
            insertStatement.setString(2, password);
            insertStatement.setString(3, role.toString());
            insertStatement.executeUpdate();

            System.out.println("User added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    // Method to remove user
    private static void removeUser(Scanner scanner, Connection connection) {
        try {
            System.out.print("\nEnter user_id to remove:");
            int userId = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            String selectQuery = "SELECT username, role FROM users WHERE user_id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, userId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String role = resultSet.getString("role");
                System.out.println("\nUser ID: " + userId);
                System.out.println("Username: " + username);
                System.out.println("Role: " + role);

                System.out.print("\nAre you sure you want to remove this user? (yes/no): ");
                String confirmation = scanner.nextLine();

                if (confirmation.equalsIgnoreCase("yes")) {
                    String deleteQuery = "DELETE FROM users WHERE user_id = ?";
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                    deleteStatement.setInt(1, userId);
                    int rowsAffected = deleteStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("User with user_id '" + userId + "' has been removed successfully.");
                    } else {
                        System.out.println("User with user_id '" + userId + "' not found.");
                    }
                } else {
                    System.out.println("Operation canceled.");
                }
            } else {
                System.out.println("User with user_id '" + userId + "' not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error removing user: " + e.getMessage());
        }
    }

}
// Enum to represent user roles

enum UserRole {
    ADMIN,
    OFFICE,
    LECTURER
}
// User class representing a user with username and role

class User {

    private String username;
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

    public void setUsername(String username) {
        this.username = username;
    }
}
