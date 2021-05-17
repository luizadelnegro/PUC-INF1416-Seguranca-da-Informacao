package models;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import controllers.MySqlController;

public class NewUser {
    
    String loginName;
    Path crtPath;
    Integer grupo;
    String salt;
    String hash;
    String cert;
    String ct;
    String blk;
    String unome;

    public boolean setCrtPath(String path) {
        crtPath = Paths.get(path);
        if (!crtPath.toFile().exists()) {
            return false;
        };
        return true;

    }

    public void setGrupo(Integer grupo) {
        this.grupo = grupo;
    }

    public static HashMap getGroupsOptions() {
        HashMap options = new HashMap();

        MySqlController mysqlsobj = MySqlController.getInstance();
        try {
            String q = String.format("SELECT gid, nome FROM Grupos;");
            ResultSet results = mysqlsobj.run_select_statement(q);
            while(results.next()) {
                options.put(results.getInt(1), results.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return options;
    }

    public Boolean saveToDb() {
        if (loginName == null || salt == null | hash == null || cert == null || grupo == null || unome == null) {
            return false;
        }
        String sql = "INSERT INTO Usuarios(login_name, salt, hash, cert, ct, blk, grupo, unome) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %d, '%s' );";

        return true;
    }
}
