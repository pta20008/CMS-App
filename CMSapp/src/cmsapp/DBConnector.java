/*
 * https://github.com/pta20008/CMS-App.git
 */
package cmsapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author bruno
 */
public class DBConnector {
    private final String DB_URL = "jdbc:mysql://localhost";
    private final String USER = "pooa2024";
    private final String PASSWORD = "pooa2024";
    
    public void createDB() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE DATABASE cms_db");
            System.out.println("DB created!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
