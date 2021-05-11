package controllers;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MySqlController {
    /* Connection details, login is specified in run-mysql-docker.bat */
    private static final String URL = "jdbc:mysql://localhost:3306/DigitalVault";
    private static final String DB_USER = "User";
    private static final String DB_PASSWORD = "6GqrPKzrTaSk";

    private static final Logger LOGGER = Logger.getLogger(MySqlController.class.getName());

    private static MySqlController instance;

    private Connection conn;

    
    public static MySqlController getInstance() {
        if (instance == null) {
            instance = new MySqlController();
        }
        return instance;
    }

    private MySqlController() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    public void openConnection() {
        Boolean isOpen = this.safeConnIsOpen();
        if(!isOpen) {
            try {
                conn = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
            } catch (SQLException e) {
                LOGGER.severe(e.getStackTrace().toString() + " " +  e.getMessage() + " " + e.getSQLState());
            }
        }
    }

    private boolean safeConnIsOpen() {
        Boolean isOpen = false;
        if (conn != null) {
            try {
                isOpen = conn.isClosed();
            } catch (SQLException e) {
                LOGGER.severe(e.getStackTrace().toString() + " " + e.getSQLState() + " " + e.getMessage());
            }
        }
        return isOpen;
    }

    public ResultSet run_select_statement(String query) throws SQLException {
        Statement stat = conn.createStatement();
        return stat.executeQuery(query);

    }

    public void closeConnection() {
        if(safeConnIsOpen()) {
            try {
                conn.rollback();  // If active transition was not commited, prefer rollback
                conn.close();
            } catch (SQLException e) {
                LOGGER.warning(e.getSQLState() + " " + e.getMessage());
            }
        }
    }
    
    

    
}
