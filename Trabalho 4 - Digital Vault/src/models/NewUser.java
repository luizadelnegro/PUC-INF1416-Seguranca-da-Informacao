package models;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAKey;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;

import controllers.MySqlController;
import controllers.X509CertificateHandler;

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
        try {
            X509CertificateHandler certH = new X509CertificateHandler(path);
            cert = ((RSAKey) certH.getPublicKey()).getModulus().toString();
        } catch (CertificateException | IOException e){
            e.printStackTrace();
            return false;
        }
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

    public boolean setPassword(String password) {
        try {
            salt = NewUser.generateSalt();
            hash = User.generateHashedPassword(password, salt);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Boolean saveToDb() {
        if (loginName == null || salt == null || hash == null || cert == null || grupo == null || unome == null) {
            return false;
        }
        String sql = "INSERT INTO Usuarios(login_name, salt, hash, cert, ct, blk, grupo, unome) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %d, '%s' );";

        return true;
    }

    public static String generateSalt() {
        String charOptions = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random random = new Random();
        while (salt.length() < 10) { 
            int index = (int) (random.nextFloat() * charOptions.length());
            salt.append(charOptions.charAt(index));
        }
        String saltString = salt.toString();
        return saltString;
    }
}
