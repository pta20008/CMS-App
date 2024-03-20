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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            User authenticatedUser = authenticateUser(scanner, connection);
            if (authenticatedUser == null) {
                System.out.println("Authentication failed. Exiting...");
                return;
            }

            displayMenu(authenticatedUser, scanner, connection);
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    private static User authenticateUser(Scanner scanner, Connection connection) {
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            String query = "SELECT username, role FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String retrievedUsername = resultSet.getString("username");
                String roleString = resultSet.getString("role");
                UserRole role = UserRole.valueOf(roleString.toUpperCase());
                return new User(retrievedUsername, role);
            } else {
                System.out.println("Invalid username or password.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error authenticating user: " + e.getMessage());
            return null;
        }
    }

    private static void displayMenu(User user, Scanner scanner, Connection connection) {
        while (true) {
            System.out.println("\n===== College Management System Menu =====");
            System.out.println("User: " + user.getUsername() + " (" + user.getRole() + ")"); // Displays the username and role of the logged-in user
            System.out.println("1. Generate Course Report");
            System.out.println("2. Generate Student Report");
            System.out.println("3. Generate Lecturer Report");

            if (user.getRole() == UserRole.OFFICE || user.getRole() == UserRole.ADMIN) {
                System.out.println("4. Change Username");
                System.out.println("5. Change Password");
            }

            if (user.getRole() == UserRole.ADMIN) {
                System.out.println("6. Add User");
            }

            System.out.println("0. Logout");

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
                    changeUsername(user, scanner, connection);
                    break;
                case 5:
                    changePassword(user, scanner, connection);
                    break;
                case 6:
                    if (user.getRole() == UserRole.ADMIN) {
                        addUser(scanner, connection);
                    } else {
                        System.out.println("Invalid choice.");
                    }
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
}

enum UserRole {
    ADMIN,
    OFFICE,
    LECTURER
}

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
