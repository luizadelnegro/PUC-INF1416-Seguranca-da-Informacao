package models;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import controllers.MySqlController;

public class User {
    
    private String userEmail;
    
    private boolean validUser = false;
    private static final Logger LOGGER = Logger.getLogger(User.class.getName());

    public User(String userEmail){
        this.userEmail = userEmail;
        isEmailValid();
    }

    private void isEmailValid() {
        MySqlController mysqlsobj = MySqlController.getInstance();
        try {
            ResultSet results = mysqlsobj.run_select_statement("SELECT * FROM user WHERE email = '" + userEmail + "'");
            
            this.validUser = results.next();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getStackTrace().toString() + " " + e.getSQLState() + " " + e.getMessage());
            this.validUser = false;
        }
    }

    public boolean isValid() {
        return this.validUser;
    }
}
