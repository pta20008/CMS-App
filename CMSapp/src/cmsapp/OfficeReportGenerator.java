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

public class OfficeReportGenerator {

    public static void generateStudentReport(Connection connection) {
        try {
            // Looking for all students
            String query = "SELECT * FROM students";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Check the results
            if (!resultSet.isBeforeFirst()) {
                System.out.println("Student not found.");
                return;
            }

            // Generate a report for each student
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

                // Check the student module enrollment 
                System.out.println("\nEnrolled Modules:");
                retrieveEnrolledModules(studentId, connection);

                // Check for finished modules and grade for each student
                System.out.println("\nCompleted Modules:");
                retrieveCompletedModules(studentId, connection);

                // Check module that need to be repeated by the student
                System.out.println("\nModules to Repeat:");
                retrieveModulesToRepeat(studentId, connection);
                
                System.out.println("-----------------------------");

                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error generating student report: " + e.getMessage());
        }
    }

    // Method to search for students enrolled modules
    private static void retrieveEnrolledModules(int studentId, Connection connection) {
        try {
            String query = "SELECT c.course_name FROM enrollments e " +
                           "JOIN courses c ON e.course_id = c.course_id " +
                           "WHERE e.student_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                System.out.println("- " + courseName);
            }
        } catch (SQLException e) {
            System.out.println("Error retriving enrolled modules: " + e.getMessage());
        }
    }

    // Method to check completed modules and student grade
    private static void retrieveCompletedModules(int studentId, Connection connection) {
        try {
            String query = "SELECT c.course_name, g.grade FROM enrollments e " +
                           "JOIN courses c ON e.course_id = c.course_id " +
                           "LEFT JOIN grades g ON e.enrollment_id = g.enrollment_id " +
                           "WHERE e.student_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                float grade = resultSet.getFloat("grade");
                System.out.println("- " + courseName + " (Grade: " + grade + ")");
            }
        } catch (SQLException e) {
            System.out.println("Error retriving completed modules: " + e.getMessage());
        }
    }

    // Method to search modules to be repeated
    private static void retrieveModulesToRepeat(int studentId, Connection connection) {
        try {
            String query = "SELECT c.course_name FROM courses c " +
                           "JOIN enrollments e ON c.course_id = e.course_id " +
                           "LEFT JOIN grades g ON e.enrollment_id = g.enrollment_id " +
                           "WHERE e.student_id = ? AND g.grade IS NULL";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                System.out.println("- " + courseName);
            }
        } catch (SQLException e) {
            System.out.println("Error retriving modules to be repeated: " + e.getMessage());
        }
    }
}
