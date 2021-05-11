package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import controllers.MySqlController;

public class ValidateDataBase {
    
    private static final Logger LOGGER = Logger.getLogger( ValidateDataBase.class.getName() );

    public static boolean EmailExists(String email){
        MySqlController mysqlsobj = MySqlController.getInstance();
        try {
            mysqlsobj.openConnection();
            ResultSet results = mysqlsobj.run_select_statement("SELECT * FROM user WHERE email = '" + email + "'");
            
            return results.next();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getStackTrace().toString() + " " + e.getSQLState() + " " + e.getMessage());
            return false;
        }
        
    }
}
