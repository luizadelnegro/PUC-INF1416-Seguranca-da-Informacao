package models;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;

import controllers.MySqlController;
import controllers.X509CertificateHandler;

public class NewUser {    
    String loginName;
    Integer grupo;
    String salt;
    String hash;
    String cert;
    Integer blk = 0;
    String unome;

    public X509CertificateHandler setCrtPath(String path) {
        Path crtPath;
        X509CertificateHandler certH = null;
        crtPath = Paths.get(path);
        if (!crtPath.toFile().exists()) {
            return null;
        };
        try {
            FileInputStream is = new FileInputStream (path);
            certH = new X509CertificateHandler(is);
            is.close();
            cert = certH.getEncoded();
            loginName = certH.getEmail();
            unome = certH.getName();
        } catch (CertificateException | IOException e){
            e.printStackTrace();
            return null;
        }
        return certH;

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
        // if (loginName == null || salt == null || hash == null || cert == null || grupo == null || unome == null) {
        //     return false;
        // }
        String sql = "INSERT INTO Usuarios(login_name, salt, hash, cert, blk, grupo, unome) VALUES ('%s', '%s', '%s', '%s', %d, %d, '%s');";
        sql = String.format(sql, loginName, salt, hash, cert, blk, grupo, unome);
        MySqlController mysqlsobj = MySqlController.getInstance();
        try {
            int results = mysqlsobj.run_insert_statement(sql);
            return results > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
