/*
 * https://github.com/pta20008/CMS-App.git
 */

package cmsapp;

/**
 * @author bruno
 */


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseReportGenerator {

    // Method to generate the report showing in the screen/console
    public static void generateCourseReport(Connection connection) {
        try {
            // Query to gather information about the courses
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

            // Print report config
            System.out.println("===== Course Report =====");
            System.out.printf("%-22s | %-40s | %-12s | %-15s | %s%n",
                    "Course Name", "Program", "Num Students", "Lecturer", "Room");
            System.out.println("-------------------------------------------------------------------------");

            // Print details for each course
            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                String program = resultSet.getString("program");
                int numStudents = resultSet.getInt("num_students");
                String lecturer = resultSet.getString("lecturer");
                String room = resultSet.getString("room");

                System.out.printf("%-22s | %-40s | %-12s | %-15s | %s%n",
                        courseName, program, numStudents, lecturer, room);
            }

            System.out.println("=========================");

            // Closing
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error generating course report: " + e.getMessage());
        }
    }
}
