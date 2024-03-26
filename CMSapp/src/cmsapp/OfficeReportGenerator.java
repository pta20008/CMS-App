/*
 * https://github.com/pta20008/CMS-App.git
 */

package cmsapp;

/**
 * @author bruno
 */

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class OfficeReportGenerator {

    // Report Menu options
    public static void generateStudentReport(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nHow do you want to display the report?");
        System.out.println("1. Print to console");
        System.out.println("2. Save to text file");
        System.out.println("3. Save to CSV file");

        System.out.print("\nEnter your choice: ");

        int option = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        switch (option) {
            case 1:
                generateStudentReportToConsole(connection);
                break;
            case 2:
                generateStudentReportToFile(connection);
                break;
            case 3:
                generateStudentReportToCSV(connection);
                break;
            default:
                System.out.println("Invalid option. Please choose 1, 2, or 3.");
                break;
        }
    }

    private static void generateStudentReportToConsole(Connection connection) {
        try {
            // Finding all students
            String query = "SELECT * FROM students";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Checking the results
            if (!resultSet.isBeforeFirst()) {
                System.out.println("No student found.");
                return;
            }

            // Generating a report for each student
            while (resultSet.next()) {
                int studentId = resultSet.getInt("student_id");
                String studentName = resultSet.getString("student_name");
                String studentNumber = resultSet.getString("student_number");
                String program = resultSet.getString("program");

                System.out.println("===== Student Report =====");
                System.out.println("Student ID: " + studentId);
                System.out.println("Name: " + studentName);
                System.out.println("Student Number: " + studentNumber);
                System.out.println("Program: " + program);

                // Checking student module enrollment
                System.out.println("\nEnrolled Modules:");
                retrieveEnrolledModules(studentId, connection);

                // Checking for completed modules and grades for each student
                System.out.println("\nCompleted Modules:");
                retrieveCompletedModules(studentId, connection);

                // Checking modules that need to be repeated by the student
                System.out.println("\nModules to Repeat:");
                retrieveModulesToRepeat(studentId, connection);

                System.out.println("=============================");

                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error generating student report: " + e.getMessage());
        }
    }

    private static void generateStudentReportToFile(Connection connection) {
        try {
            FileWriter writer = new FileWriter("Student_Report.txt");

            // Finding all students
            String query = "SELECT * FROM students";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Checking the results
            if (!resultSet.isBeforeFirst()) {
                writer.write("No student found.");
                writer.close();
                return;
            }

            // Generating a report for each student
            while (resultSet.next()) {
                int studentId = resultSet.getInt("student_id");
                String studentName = resultSet.getString("student_name");
                String studentNumber = resultSet.getString("student_number");
                String program = resultSet.getString("program");

                writer.write("===== Student Report =====\n");
                writer.write("Student ID: " + studentId + "\n");
                writer.write("Name: " + studentName + "\n");
                writer.write("Student Number: " + studentNumber + "\n");
                writer.write("Program: " + program + "\n");

                // Checking student module enrollment
                writer.write("\nEnrolled Modules:\n");
                writeEnrolledModulesToFile(studentId, writer, connection);

                // Checking for completed modules and grades for each student
                writer.write("\nCompleted Modules:\n");
                writeCompletedModulesToFile(studentId, writer, connection);

                // Checking modules that need to be repeated by the student
                writer.write("\nModules to Repeat:\n");
                writeModulesToRepeatToFile(studentId, writer, connection);

                writer.write("==============================\n\n");
            }

            writer.close();
            System.out.println("Report written to file: Student_Report.txt");
        } catch (IOException | SQLException e) {
            System.out.println("Error generating student report: " + e.getMessage());
        }
    }

    private static void generateStudentReportToCSV(Connection connection) {
        try {
            FileWriter writer = new FileWriter("Student_Report.csv");

            // Writing header
            writer.write("Student ID,Name,Student Number,Program,Enrolled Modules,Completed Modules,Modules to Repeat\n");

            // Finding all students
            String query = "SELECT * FROM students";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Generating a report for each student
            while (resultSet.next()) {
                int studentId = resultSet.getInt("student_id");
                String studentName = resultSet.getString("student_name");
                String studentNumber = resultSet.getString("student_number");
                String program = resultSet.getString("program");

                StringBuilder enrolledModules = new StringBuilder();
                StringBuilder completedModules = new StringBuilder();
                StringBuilder modulesToRepeat = new StringBuilder();

                // Retrieving student data
                retrieveEnrolledModulesToString(studentId, enrolledModules, connection);
                retrieveCompletedModulesToString(studentId, completedModules, connection);
                retrieveModulesToRepeatToString(studentId, modulesToRepeat, connection);

                // Writing student data to CSV
                writer.write(String.format("%d,%s,%s,%s,%s,%s,%s\n", studentId, studentName, studentNumber, program,
                        enrolledModules.toString(), completedModules.toString(), modulesToRepeat.toString()));
            }

            writer.close();
            System.out.println("Report written to file: Student_Report.csv");
        } catch (IOException | SQLException e) {
            System.out.println("Error generating student report: " + e.getMessage());
        }
    }

    // Method to retrieve students' enrolled modules
    private static void retrieveEnrolledModules(int studentId, Connection connection) {
        try {
            String query = "SELECT c.course_name FROM enrollments e "
                    + "JOIN courses c ON e.course_id = c.course_id "
                    + "WHERE e.student_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                System.out.println("- " + courseName);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving enrolled modules: " + e.getMessage());
        }
    }

    // Method to write students' enrolled modules to file
    private static void writeEnrolledModulesToFile(int studentId, FileWriter writer, Connection connection) throws IOException {
        try {
            String query = "SELECT c.course_name FROM enrollments e "
                    + "JOIN courses c ON e.course_id = c.course_id "
                    + "WHERE e.student_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                writer.write("- " + courseName + "\n");
            }
        } catch (SQLException e) {
            writer.write("Error retrieving enrolled modules: " + e.getMessage() + "\n");
        }
    }

    // Method to retrieve students' enrolled modules as a string
    private static void retrieveEnrolledModulesToString(int studentId, StringBuilder stringBuilder, Connection connection) {
        try {
            String query = "SELECT c.course_name FROM enrollments e "
                    + "JOIN courses c ON e.course_id = c.course_id "
                    + "WHERE e.student_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                stringBuilder.append(courseName).append(", ");
            }
            // Remove trailing comma and space
            if (stringBuilder.length() > 2) {
                stringBuilder.setLength(stringBuilder.length() - 2);
            }
        } catch (SQLException e) {
            stringBuilder.append("Error retrieving enrolled modules: ").append(e.getMessage());
        }
    }

    // Method to check completed modules and student grades
    private static void retrieveCompletedModules(int studentId, Connection connection) {
        try {
            String query = "SELECT c.course_name, g.grade FROM enrollments e "
                    + "JOIN courses c ON e.course_id = c.course_id "
                    + "LEFT JOIN grades g ON e.enrollment_id = g.enrollment_id "
                    + "WHERE e.student_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                float grade = resultSet.getFloat("grade");
                System.out.println("- " + courseName + " (Grade: " + grade + ")");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving completed modules: " + e.getMessage());
        }
    }

    // Method to write completed modules and student grades to file
    private static void writeCompletedModulesToFile(int studentId, FileWriter writer, Connection connection) throws IOException {
        try {
            String query = "SELECT c.course_name, g.grade FROM enrollments e "
                    + "JOIN courses c ON e.course_id = c.course_id "
                    + "LEFT JOIN grades g ON e.enrollment_id = g.enrollment_id "
                    + "WHERE e.student_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                float grade = resultSet.getFloat("grade");
                writer.write("- " + courseName + " (Grade: " + grade + ")\n");
            }
        } catch (SQLException e) {
            writer.write("Error retrieving completed modules: " + e.getMessage() + "\n");
        }
    }

    // Method to retrieve completed modules and student grades as a string
    private static void retrieveCompletedModulesToString(int studentId, StringBuilder stringBuilder, Connection connection) {
        try {
            String query = "SELECT c.course_name, g.grade FROM enrollments e "
                    + "JOIN courses c ON e.course_id = c.course_id "
                    + "LEFT JOIN grades g ON e.enrollment_id = g.enrollment_id "
                    + "WHERE e.student_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                float grade = resultSet.getFloat("grade");
                stringBuilder.append(courseName).append(" (Grade: ").append(grade).append("), ");
            }
            // Remove trailing comma and space
            if (stringBuilder.length() > 2) {
                stringBuilder.setLength(stringBuilder.length() - 2);
            }
        } catch (SQLException e) {
            stringBuilder.append("Error retrieving completed modules: ").append(e.getMessage());
        }
    }

    // Method to retrieve modules that need to be repeated
    private static void retrieveModulesToRepeat(int studentId, Connection connection) {
        try {
            String query = "SELECT c.course_name FROM courses c "
                    + "JOIN enrollments e ON c.course_id = e.course_id "
                    + "LEFT JOIN grades g ON e.enrollment_id = g.enrollment_id "
                    + "WHERE e.student_id = ? AND (g.grade < 40 OR g.grade IS NULL)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);

            ResultSet resultSet = statement.executeQuery();

            boolean modulesFound = false;

            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                System.out.println("- " + courseName);
                modulesFound = true;
            }

            if (!modulesFound) {
                System.out.println("None");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving modules to be repeated: " + e.getMessage());
        }
    }

    // Method to write modules that need to be repeated to file
    private static void writeModulesToRepeatToFile(int studentId, FileWriter writer, Connection connection) throws IOException {
        try {
            String query = "SELECT c.course_name FROM courses c "
                    + "JOIN enrollments e ON c.course_id = e.course_id "
                    + "LEFT JOIN grades g ON e.enrollment_id = g.enrollment_id "
                    + "WHERE e.student_id = ? AND (g.grade < 40 OR g.grade IS NULL)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);

            ResultSet resultSet = statement.executeQuery();

            boolean modulesFound = false;

            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                writer.write("- " + courseName + "\n");
                modulesFound = true;
            }

            if (!modulesFound) {
                writer.write("None\n");
            }

        } catch (SQLException e) {
            writer.write("Error retrieving modules to be repeated: " + e.getMessage() + "\n");
        }
    }

    // Method to retrieve modules that need to be repeated as a string
    private static void retrieveModulesToRepeatToString(int studentId, StringBuilder stringBuilder, Connection connection) {
        try {
            String query = "SELECT c.course_name FROM courses c "
                    + "JOIN enrollments e ON c.course_id = e.course_id "
                    + "LEFT JOIN grades g ON e.enrollment_id = g.enrollment_id "
                    + "WHERE e.student_id = ? AND (g.grade < 40 OR g.grade IS NULL)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);

            ResultSet resultSet = statement.executeQuery();

            boolean modulesFound = false;

            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                stringBuilder.append(courseName).append(", ");
                modulesFound = true;
            }

            if (!modulesFound) {
                stringBuilder.append("None");
            } else {
                // Remove trailing comma and space
                stringBuilder.setLength(stringBuilder.length() - 2);
            }

        } catch (SQLException e) {
            stringBuilder.append("Error retrieving modules to be repeated: ").append(e.getMessage());
        }
    }
}
