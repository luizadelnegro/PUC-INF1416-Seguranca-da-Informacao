package models;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import controllers.MySqlController;

public class User {
    
    private String userEmail;
    
    private boolean validUser = false;
    private Integer groupId = null;
    private String groupName = null;
    private String name = null;
    private Integer totalAcessos = null;
    private Integer totalConsultas = null;

    private static final Logger LOGGER = Logger.getLogger(User.class.getName());

    public User(String userEmail){
        this.userEmail = userEmail;
        isEmailValid();
    }

    private void isEmailValid() {
        MySqlController mysqlsobj = MySqlController.getInstance();
        try {
            ResultSet results = mysqlsobj.run_select_statement("SELECT * FROM Usuarios WHERE login_name = '" + userEmail + "';");
            this.validUser = results.next();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getStackTrace().toString() + " " + e.getSQLState() + " " + e.getMessage());
            this.validUser = false;
        }
    }

    public boolean isValid() {
        return this.validUser;
    }

    private void updateGroupValues() {
        MySqlController mysqlsobj = MySqlController.getInstance();
        try {
            String q = String.format("SELECT g.nome, g.gid FROM Usuarios u JOIN Grupos g ON g.gid = u.grupo WHERE login_name = '%s';", userEmail);
            ResultSet results = mysqlsobj.run_select_statement(q);
            results.next();
            groupName = results.getString(1);
            groupId = results.getInt(2);
        } catch (SQLException e) {
            e.printStackTrace();
            groupId = 2;
        }
    }

    public String getGroupName() {
        if(groupName == null) {
            updateGroupValues();
        }
        return groupName;
    }

    public boolean isAdmin() {
        if(groupId == null) {
            updateGroupValues();
        }
        return groupId == 1;
    }

    public String getName() {
        MySqlController mysqlsobj = MySqlController.getInstance();
        if(name == null) {
            try {
                String q = String.format("SELECT unome FROM Usuarios WHERE login_name = '%s';", userEmail);
                ResultSet results = mysqlsobj.run_select_statement(q);
                results.next();
                name = results.getString(1);
            } catch (SQLException e) {
                e.printStackTrace();
                name = "";
            }
        }
        return name;
    }

    public String getLoginName() {
        return userEmail;
    }

    public Integer getTotalDeAcessos() {
        MySqlController mysqlsobj = MySqlController.getInstance();
        if(totalAcessos == null) {
            try {
                String q = String.format("SELECT count(*) FROM Registros WHERE mensagem_id = 4003 AND login_name = '%s';", userEmail);
                ResultSet results = mysqlsobj.run_select_statement(q);
                results.next();
                totalAcessos = results.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
                totalAcessos = 0;
            }
        }
        return totalAcessos;
    }

    public Integer getTotalUsuarios() {
        MySqlController mysqlsobj = MySqlController.getInstance();
        if (!isAdmin()) return 0;
        try {
            String q = String.format("SELECT count(*) FROM Usarios;");
            ResultSet results = mysqlsobj.run_select_statement(q);
            results.next();
            return results.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    } 

    public Integer getTotalConsultasDeAcessos() {
        MySqlController mysqlsobj = MySqlController.getInstance();
        if(totalConsultas == null) {
            try {
                String q = String.format("SELECT count(*) FROM Registros WHERE mensagem_id = 5004 AND login_name = '%s';", userEmail);
                ResultSet results = mysqlsobj.run_select_statement(q);
                results.next();
                totalConsultas = results.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
                totalConsultas = 0;
            }
        }
        return totalConsultas;
    }
}
