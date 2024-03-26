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

public class CourseReportGenerator {

    public static void generateCourseReport(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nHow do you want to display the report?");
        System.out.println("1. Print to console");
        System.out.println("2. Save to text file");
        System.out.print("\nEnter your choice: ");

        int option = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        switch (option) {
            case 1:
                generateCourseReportToConsole(connection);
                break;
            case 2:
                generateCourseReportToFile(connection);
                break;
            default:
                System.out.println("Invalid option. Please choose 1 or 2.");
                break;
        }
    }

    // Method to generate the course report to console
    private static void generateCourseReportToConsole(Connection connection) {
        try {
            // Query to retrieve course information
            String query = "SELECT course_name, program, COUNT(e.student_id) AS num_students, lecturer, " +
                    "CASE " +
                    "   WHEN room = 'Online' THEN 'Online' " +
                    "   ELSE room " +
                    "END AS room " +
                    "FROM courses c " +
                    "LEFT JOIN enrollments e ON c.course_id = e.course_id " +
                    "GROUP BY c.course_id";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Checking for results
            if (!resultSet.isBeforeFirst()) {
                System.out.println("No courses found.");
                return;
            }

            // Printing the report header
            System.out.println("======= Course Report =======");
            System.out.printf("%-24s | %-40s | %-12s | %-15s | %s%n",
                    "Course Name", "Program", "Num Students", "Lecturer", "Room");
            System.out.println("-------------------------------------------------------------------------");

            // Printing details of each course
            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                String program = resultSet.getString("program");
                int numStudents = resultSet.getInt("num_students");
                String lecturer = resultSet.getString("lecturer");
                String room = resultSet.getString("room");

                System.out.printf("%-24s | %-40s | %-12s | %-15s | %s%n",
                        courseName, program, numStudents, lecturer, room);
            }

            System.out.println("=========================");

            // Closing resources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error generating course report: " + e.getMessage());
        }
    }

    // Method to generate the course report to a text file
    private static void generateCourseReportToFile(Connection connection) {
        try {
            FileWriter writer = new FileWriter("Course_Report.txt");

            // Query to retrieve course information
            String query = "SELECT course_name, program, COUNT(e.student_id) AS num_students, lecturer, " +
                    "CASE " +
                    "   WHEN room = 'Online' THEN 'Online' " +
                    "   ELSE room " +
                    "END AS room " +
                    "FROM courses c " +
                    "LEFT JOIN enrollments e ON c.course_id = e.course_id " +
                    "GROUP BY c.course_id";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Checking for results
            if (!resultSet.isBeforeFirst()) {
                writer.write("No courses found.");
                writer.close();
                return;
            }

            // Writing the report header to the file
            writer.write("======= Course Report =======\n");
            writer.write(String.format("%-24s | %-40s | %-12s | %-15s | %s%n",
                    "Course Name", "Program", "Num Students", "Lecturer", "Room"));
            writer.write("-------------------------------------------------------------------------\n");

            // Writing details of each course to the file
            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                String program = resultSet.getString("program");
                int numStudents = resultSet.getInt("num_students");
                String lecturer = resultSet.getString("lecturer");
                String room = resultSet.getString("room");

                writer.write(String.format("%-24s | %-40s | %-12s | %-15s | %s%n",
                        courseName, program, numStudents, lecturer, room));
            }

            writer.write("=========================\n");

            // Closing resources
            resultSet.close();
            statement.close();
            writer.close();
            System.out.println("Report written to file: Course_Report.txt");
        } catch (IOException | SQLException e) {
            System.out.println("Error generating course report: " + e.getMessage());
        }
    }
}
