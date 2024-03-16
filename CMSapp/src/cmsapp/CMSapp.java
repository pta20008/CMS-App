/*
 * https://github.com/pta20008/CMS-App.git
 */
package cmsapp;

import java.sql.SQLException;

/**
 *
 * @author bruno
 */
public class CMSapp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        DBConnector db = new DBConnector();
        db.createDB();
    }
    
}
