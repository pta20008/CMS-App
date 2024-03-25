/*
 * https://github.com/pta20008/CMS-App.git
 */

package cmsapp;

/**
 * @author bruno
**/

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class LecturerReportGenerator {

    public static void generateLecturerReport(String username, Connection connection) {
        try {
            // Check info about lectures and students count
            String query = "SELECT c.lecturer, c.program, COUNT(e.student_id) AS students_count " +
                           "FROM courses c " +
                           "INNER JOIN enrollments e ON c.course_id = e.course_id " +
                           "GROUP BY c.lecturer, c.program";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Check if there are results
            if (!resultSet.isBeforeFirst()) {
                System.out.println("No data found for lecturer report.");
                return;
            }

            // Report options
            System.out.println("\nHow do you want to display the report?");
            System.out.println("1. Print on screen");
            System.out.println("2. Save to text file");
            System.out.println("3. Save to CSV file");

            System.out.print("\nEnter your choice: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.nextLine();

            // Check user choice
            switch (choice) {
                case 1:
                    printReport(resultSet);
                    break;
                case 2:
                    saveReportToTextFile(resultSet);
                    break;
                case 3:
                    saveReportToCSVFile(resultSet);
                    break;
                default:
                    System.out.println("Invalid choice. Printing on screen by default.");
                    printReport(resultSet);
                    break;
            }
        } catch (SQLException e) {
            System.out.println("Error generating lecturer report: " + e.getMessage());
        }
    }

    // Method to print the report on screen
    private static void printReport(ResultSet resultSet) throws SQLException {
        System.out.println("\n===== Lecturer Report =====");
        while (resultSet.next()) {
            String lecturerName = resultSet.getString("lecturer");
            String program = resultSet.getString("program");
            int studentsCount = resultSet.getInt("students_count");

            System.out.println("Lecturer: " + lecturerName);
            System.out.println("Program: " + program);
            System.out.println("Students Count: " + studentsCount);
            System.out.println("-----------------------------");
        }
    }

    // Method to save the report to a text file (.txt)
    private static void saveReportToTextFile(ResultSet resultSet) throws SQLException {
        try (FileWriter writer = new FileWriter("lecturer_report.txt")) {
            writer.write("===== Lecturer Report =====\n");
            while (resultSet.next()) {
                String lecturerName = resultSet.getString("lecturer");
                String program = resultSet.getString("program");
                int studentsCount = resultSet.getInt("students_count");

                writer.write("Lecturer: " + lecturerName + "\n");
                writer.write("Program: " + program + "\n");
                writer.write("Students Count: " + studentsCount + "\n");
                writer.write("-----------------------------\n");
            }
            System.out.println("Report saved to lecturer_report.txt");
        } catch (IOException e) {
            System.out.println("Error saving report to file: " + e.getMessage());
        }
    }

    // Method to save the report to a CSV file (.csv)
    private static void saveReportToCSVFile(ResultSet resultSet) throws SQLException {
        try (FileWriter writer = new FileWriter("lecturer_report.csv")) {
            // Write header to CSV file
            writer.write("Lecturer,Program,Students Count\n");

            // Write data rows to CSV file
            while (resultSet.next()) {
                String lecturerName = resultSet.getString("lecturer");
                String program = resultSet.getString("program");
                int studentsCount = resultSet.getInt("students_count");

                // Format the row as CSV format
                String csvRow = String.format("%s,%s,%d\n", lecturerName, program, studentsCount);
                writer.write(csvRow);
            }
            System.out.println("Report saved to lecturer_report.csv");
        } catch (IOException e) {
            System.out.println("Error saving report to CSV file: " + e.getMessage());
        }
    }
}
