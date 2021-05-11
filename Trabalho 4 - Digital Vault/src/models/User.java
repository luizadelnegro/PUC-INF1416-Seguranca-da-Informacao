package models;
import java.util.logging.Level;
import java.util.logging.Logger;

import controllers.ValidateDataBase;

public class User {
    
    private String user_email;
    
    private boolean fail_user = false;

    private static final Logger LOGGER = Logger.getLogger(User.class.getName());

    public User(String user_email){

        if (ValidateDataBase.EmailExists(user_email)) {
            this.user_email = user_email;
        }
        else {
            LOGGER.log(Level.WARNING, "User failed! Unknown email " + user_email);
            this.fail_user = true;
        }
    }

    public boolean isValid() {
        return ! this.fail_user;
    }
}
