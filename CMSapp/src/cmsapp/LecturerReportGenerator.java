/*
 * https://github.com/pta20008/CMS-App.git
 */
package cmsapp;

/**
 *
 * @author bruno
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LecturerReportGenerator {

    public static void generateLecturerReport(String username, Connection connection) {
        try {
            // Consulta para recuperar informações dos professores e contagem de alunos
            String query = "SELECT c.lecturer, c.program, COUNT(e.student_id) AS students_count " +
                           "FROM courses c " +
                           "INNER JOIN enrollments e ON c.course_id = e.course_id " +
                           "GROUP BY c.lecturer, c.program";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Verificar se há resultados
            if (!resultSet.isBeforeFirst()) {
                System.out.println("No data found for lecturer report.");
                return;
            }

            // Exibir o relatório
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
        } catch (SQLException e) {
            System.out.println("Error generating lecturer report: " + e.getMessage());
        }
    }
}